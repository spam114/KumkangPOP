package com.symbol.kumkangpop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/*
*   재사용 가능한 RecyclerView Model
* */

class RecyclerViewModel(private val api: Int? = null): ViewModel() {

    /*// 데이터 로드 실패시 에러 이벤트
    private val _navigateToLoadFail = SingleLiveEvent<Any>()
    val navigateToLoadFail : LiveData<Any>
        get() = _navigateToLoadFail

    // 페이징이 끝났을 때 이벤트
    private val _pagingEnd = SingleLiveEvent<Any>()
    val pagingEnd : LiveData<Any>
        get() = _pagingEnd

    // 아이템 개수 Live Data
    private val _count = MutableLiveData<Int>()
    val itemCount : LiveData<Int> = _count

    // 데이터를 추가할 것인지 초기화할 것인지 설정하는 변수
    private var mode = INIT

    // Live Data 갱신을 위해 데이터목록을 따로 관리
    private var list = mutableListOf<Any?>()

    private val _dataList = MutableLiveData<List<Any?>>()
    val dataList: LiveData<List<Any?>> = _dataList

    companion object{
        // 페이징을 위한 변수
        const val ADD = -1  // 기존 데이터에 덧붙인다
        const val INIT = -2 // 기존 데이터를 덮어씌운다

        // API 변수
        const val exampleApi1 = 0
        const val exampleApi2 = 1

    }

    // 데이터 리스트 받았을 때 공통 처리 call back 함수
    private val callback = object: BaseNetworkInterface.SimpleListDataResponseCallback{
        override fun onSuccess(list: List<Any?>) {
            if(list.isEmpty()){
                // 불러올 데이터가 더이상 없으면 이벤트 호출한다
                _pagingEnd.call()
            }
            else if(mode == ADD){
                // 페이징 데이터를 추가한다
                this@RecyclerViewModel.list.addAll(list)
            }
            else{
                // 현재 데이터를 초기화한다
                this@RecyclerViewModel.list = list.toMutableList()

            }
            _dataList.value = this@RecyclerViewModel.list

        }

        override fun onFailure(statusCode: Int?) {
            _navigateToLoadFail.call()
        }

    }

    // 데이터 갯수가 포함된 데이터 리스트를 처리하는 콜백함수
    private val countCallback = object: BaseNetworkInterface.CountListDataResponseCallback{
        override fun onSuccess(list: List<Any?>, count: Int) {
            if(list.isEmpty()){
                // 불러올 데이터가 더이상 없으면 이벤트 호출한다
                _pagingEnd.call()
            }
            else if(mode == ADD){
                // 페이징 데이터를 추가한다
                this@RecyclerViewModel.list.addAll(list)
            }
            else{
                // 현재 데이터를 초기화한다
                this@RecyclerViewModel.list = list.toMutableList()

            }
            _dataList.value = this@RecyclerViewModel.list
            _count.value = count

        }

        override fun onFailure(statusCode: Int?) {
            _navigateToLoadFail.call()
        }

    }


    // 설정한 API의 첫번째 페이지를 불러오는 메서드
    fun init(api : Int? = this.api, page: Int? = 1, mode: Int? = INIT){
        //  Recycler View Model 생성시 어떤  API를 호출할 것인지 설정한다
        api?.let {
            this.mode = mode ?: INIT
            when(api){

                exampleApi1 -> {
                    requestMainBannerApi(mode = mode, page = page)
                }

                exampleApi2  ->  {
                    requestFeaturedProductApi(mode =mode, page = page)
                }
            }
        }
    }


    // 아이템을 삭제한다
    fun delete(item: Any, api: Int? = null){
        // list에서 아이템을 삭제한다
        list.remove(item)
        _dataList.value = list

        // API 호출이 있는 경우 알맞은 것을 호출한다
        when(api){
            deleteRecentKeyword -> {
                deleteRecentKeyword(item)
            }

        }
    }


    // 다음 페이징 아이템을 불러온다
    fun next(page: Int){
        init(api = this.api, mode = ADD, page= page)
    }


    // 예시 API 호출
    private fun exampleApi1(page: Int? = 1, mode: Int? = INIT){
        // 데이터 호출 로직
    }

    // 예시 API 호출
    private fun exampleApi2(page: Int? = 1, mode: Int? = INIT){
        // 데이터 호출 로직
    }*/
}