//package com.lazy.component.base;
//
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.lazy.component.commpoentbase.R;
//import com.lazy.component.widget.SpacesItemDecoration;
//
//import java.lang.reflect.Field;
//import java.util.List;
//
//
///**
// * 所有list的基类
// *
// * @author zdxiang
// * @date 2018/8/6/006 13:39
// */
//public abstract class BaseListFragment<A extends BaseQuickAdapter, P extends BasePresenter> extends BaseFragment<P> implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {
//
//    @BindView(R.id.srl)
//    protected SwipeRefreshLayout srl;
//
//    @BindView(R.id.recyclerView)
//    protected RecyclerView recyclerView;
//
//    protected int mPageIndex = 1;
//
//    /**
//     * default pageSize
//     */
//    protected int mPageSize = 10;
//
//    protected A mAdapter;
//
//    /**
//     * 由于后台的奇葩分页逻辑需要lastTime这个东西，加载第二页时需要取第一页的最后一条的字段lastTime
//     * default is empty
//     */
//    protected String mLastTime = "";
//
//    @Override
//    public void onFirstUserVisible() {
//
//    }
//
//    /**
//     * <p>初始化view都在这里,注意，这个方法是有可能出现在init方法里面，也可能是在onFirstUserVisible方法里面，根据需求不同而定。
//     * 当如果当前fragment是在viewpager中并且使用懒加载，那就建议放onFirstUserVisible，反之则放init方法。
//     * 如果在懒加载的情况下，并且viewPager设置了setOffscreenPageLimit(fragments.size());,如果放init方法，这个initView方法会在所有fragment下都初始化
//     * <p/>
//     */
//    public void initView() {
//        srl.setOnRefreshListener(this);
//        srl.setEnabled(isRefreshEnable());
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new SpacesItemDecoration(2));
//        mAdapter = getAdapter();
//        recyclerView.setAdapter(mAdapter);
//        if (isLoadMoreEnable()) {
//            mAdapter.setOnLoadMoreListener(this, recyclerView);
//        }
//        mAdapter.setOnItemClickListener(this);
//    }
//
//    /**
//     * 检查当前list是否为空
//     */
//    public void checkEmpty() {
//        if (mAdapter != null) {
//            if (mAdapter.getData().size() == 0) {
//                mAdapter.setNewData(null);
//                mAdapter.setEmptyView(R.layout.widget_empty_view, (ViewGroup) recyclerView.getParent());
//            }
//        }
//    }
//
//    protected abstract A getAdapter();
//
//    /**
//     * 设置是否可以下拉刷新
//     *
//     * @return default is true
//     */
//    public boolean isRefreshEnable() {
//        return true;
//    }
//
//    /**
//     * 配置当前页面是否需要加载更多的功能
//     * 不需要，请复写返回false
//     * default is true
//     *
//     * @return true
//     */
//    public boolean isLoadMoreEnable() {
//        return true;
//    }
//
//    /**
//     * 重置页码
//     */
//    protected void resetPage() {
//        mPageIndex = 1;
//    }
//
//    /**
//     * 刷新数据需要重置lastTIme
//     */
//    protected void resetLastTime() {
//        mLastTime = "";
//    }
//
//    /**
//     * 这里放数据请求
//     */
//    public void getData() {
//
//    }
//
//    @Override
//    public void onRefresh() {
//        setIfNeedShowLoadingDialog(false);
//        resetPage();
//        resetLastTime();
//        getData();
//    }
//
//    @Override
//    public void onLoadMoreRequested() {
//        setIfNeedShowLoadingDialog(false);
//        getData();
//    }
//
//    @Override
//    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//    }
//
//
//    /**
//     * 自动加载数据,包含加载更多
//     *
//     * @param list list
//     */
//    public void setDataAuto(List<?> list) {
//        if (mPageIndex == 1) {
//            if (list == null || list.size() == 0) {
//                mAdapter.setNewData(null);
//                mAdapter.loadMoreEnd(true);
//                mAdapter.setEmptyView(R.layout.widget_empty_view, (ViewGroup) recyclerView.getParent());
//
////
//            } else {
//                if (list.size() < 10) {
//                    mAdapter.setNewData(list);
//                    mAdapter.loadMoreEnd();
//                } else {
//                    mAdapter.setNewData(list);
//                    mAdapter.loadMoreComplete();
//                    mPageIndex++;
//                }
//                mLastTime = getLastTime(list);
//            }
//
//        } else {
//            if (list == null || list.size() == 0) {
//                mAdapter.loadMoreEnd();
//            } else {
//                if (list.size() < 10) { // TODO: 2018/8/6/006 这里将会出现问题，待检验修复
//                    mAdapter.loadMoreEnd();
//                }
//                mAdapter.addData(list);
//                mAdapter.loadMoreComplete();
//                mPageIndex++;
//                mLastTime = getLastTime(list);
//            }
//        }
//    }
//
//    private String getLastTime(List<?> list) {
//        String targetLastTime = "";
//        int targetIndex = list.size() - 1;
//        Field[] fields = list.get(targetIndex).getClass().getDeclaredFields();
//        Object o = list.get(targetIndex);
//        if (o == null) return "";
//        for (int j = 0; j < fields.length; j++) {
//            if (!fields[j].isAccessible()) {
//                fields[j].setAccessible(true);
//            }
//            try {
//                //获取list最后一个对象的指定属性值的值
//                if (fields[j].getName().equals("LastTime")) {
//                    targetLastTime = fields[j].get(o).toString();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return targetLastTime;
//    }
//
//
//    @Override
//    public void dismissLoadingDialog() {
//        super.dismissLoadingDialog();
//        if (srl != null && srl.isRefreshing()) {
//            srl.setRefreshing(false);
//        }
//    }
//}
