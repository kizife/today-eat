import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.base.BaseFragment
import com.ssafy.smartstore_jetpack.data.local.SharedPreferencesUtil
import com.ssafy.smartstore_jetpack.data.model.dto.Combination
import com.ssafy.smartstore_jetpack.data.model.dto.UserTodayeat
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.databinding.FragmentHomeBinding
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.category.CategorySoup
import com.ssafy.smartstore_jetpack.ui.home.BannerAdapter
import com.ssafy.smartstore_jetpack.ui.home.ClickCombiFragment
import com.ssafy.smartstore_jetpack.ui.my.MyPageFragment
import com.ssafy.smartstore_jetpack.ui.order.ShoppingListFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import kotlin.random.Random

private const val TAG = "HomeFragment_싸피"
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainActivity: MainActivity
    private lateinit var id: String
    private lateinit var user: UserTodayeat
    private lateinit var recyclerView: RecyclerView
    private lateinit var combiAdapter: CombinationAdapter
    private var isActivityAttached = false
    private lateinit var itemrecyclerView: RecyclerView
    private lateinit var recommendAdapter: RecommendAdapter

    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var bannerHandler: android.os.Handler
    private lateinit var bannerRunnable: Runnable
    private val bannerImages = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        isActivityAttached = true
    }

    override fun onDetach() {
        super.onDetach()
        isActivityAttached = false  // Activity와의 연결이 끊어졌음을 표시
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getRecomendCombi()
        getCombi()
        initUserData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            requireActivity().finish()
            }
        })

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }


    private fun getRecomendCombi() {
        val recommendCombiList: MutableList<Combination> = mutableListOf()
        val deferredList = mutableListOf<Deferred<Result<Combination>>>()

        val numbers = (0..14).toMutableList()

        // repeat 5번 반복
        repeat(5) {
            val randomIndex = Random.nextInt(numbers.size)
            val randomNumber = numbers[randomIndex]
            numbers.removeAt(randomIndex)

            val deferred = CoroutineScope(Dispatchers.IO).async {
                // Retrofit을 통한 데이터 요청
                runCatching {
                    RetrofitUtil.combiService.getCombi(randomNumber) // 선택된 랜덤 숫자를 이용한 API 요청
                }.onSuccess {
                    recommendCombiList.add(it)
                }.onFailure {
                    Log.d(TAG, "getRecomendCombi: ${it.message}")
                }
            }

            deferredList.add(deferred)
        }

        // 모든 비동기 작업이 완료된 후 실행
        CoroutineScope(Dispatchers.Main).launch {
            try {
                deferredList.awaitAll()  // 모든 비동기 작업이 완료될 때까지 대기

                // 데이터가 모두 로드된 후 UI 업데이트
                Log.d(TAG, "getRecomendCombi: $recommendCombiList")

                // 어댑터 설정
                if (isActivityAttached) {
                    recommendAdapter = RecommendAdapter(recommendCombiList, requireActivity()) { combi, type ->
                        navigateToDetailFragment(combi, type)
                    }
                    itemrecyclerView.adapter = recommendAdapter
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error while waiting for deferred tasks: ${e.message}")
            }
        }
    }


    private fun setupBanner() {
        bannerAdapter = BannerAdapter(bannerImages) { position->
            when(position) {
                0 -> {
                    (activity as MainActivity).opneFragmentBanner("Side")
                }
                1 -> {
                    (activity as MainActivity).opneFragmentBanner("Side")
                }
                2 -> {
                    (activity as MainActivity).opneFragmentBanner("Soup")
                }
            }
        }
        binding.banner.adapter = bannerAdapter


        bannerHandler = android.os.Handler(Looper.getMainLooper())
        bannerRunnable = Runnable {
            val currentItem = binding.banner.currentItem
            binding.banner.currentItem = currentItem +1
        }

        binding.banner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //페이지 변경시 롤링 딜레이 초기화
                bannerHandler.removeCallbacks(bannerRunnable)
                bannerHandler.postDelayed(bannerRunnable, 3000)
            }
        })
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // recyclerView 초기화
        recyclerView = binding.recyclerViewNoticeOrder  // 먼저 초기화
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        itemrecyclerView = binding.recyclerViewLatestOrder // binding 객체를 안전하게 사용

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        Log.d(TAG, "onViewCreated: $user")
        binding.nameTextHome.setText("${user.name}님,이런 조합은 어때요?")
        binding.nameTextCombi.setText("${user.name}님의 단골 레시피!")

        setupBanner()
    }

    private fun initUserData() {
        user = ApplicationClass.sharedPreferencesUtil.getUser()
        id = user.id
    }

    private var isCombiLoading = false

    private fun getCombi() {
        // 로딩 중이면 함수 종료
        if (isCombiLoading) {
            return
        }

        Log.d(TAG, "getCombi: ????")

        // 로딩 상태 설정
        isCombiLoading = true

        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                RetrofitUtil.combiService.getCombiList(SharedPreferencesUtil(requireContext()).getUser().id)
            }.onSuccess { combiList ->
                if (isActivityAttached) {
                    combiAdapter = CombinationAdapter(combiList, requireActivity()) { combi, type ->
                        navigateToDetailFragment(combi, type)
                    }
                    recyclerView.adapter = combiAdapter
                    Log.d(TAG, "getCombi: $combiList")
                } else {
                    Log.w(TAG, "getCombi: Fragment is not attached to an activity")
                }
            }.onFailure {
                Log.d(TAG, "getCombi: ${it.message}")
            }.also {
                // 로딩 완료 후 상태 업데이트
                isCombiLoading = false
            }
        }
    }


    private fun navigateToDetailFragment(combi: Combination,type:Int) {
        val newFragment = ClickCombiFragment().apply { // ()로 인스턴스 생성
            arguments = Bundle().apply {
                putParcelable("clickedCombi", combi)
                putInt("type",type)
            }
        }

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_page_3

        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, newFragment) // `frame_layout_main`은 대상 레이아웃 ID
            .addToBackStack(null) // 백스택에 추가
            .commit()


    }



    override fun onResume() {
        super.onResume()
        bannerHandler.postDelayed(bannerRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        bannerHandler.removeCallbacks(bannerRunnable)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        bannerHandler.removeCallbacks(bannerRunnable)
    }


}