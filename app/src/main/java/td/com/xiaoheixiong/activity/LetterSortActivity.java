package td.com.xiaoheixiong.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.search.City;
import td.com.xiaoheixiong.search.ClearEditText;
import td.com.xiaoheixiong.search.ItemBeanAdapter;
import td.com.xiaoheixiong.search.MyCityDBHelper;
import td.com.xiaoheixiong.search.MyLetterSortView;
import td.com.xiaoheixiong.search.SearchCityAdapter;
import td.com.xiaoheixiong.search.T;


public class LetterSortActivity extends Activity implements OnClickListener {

    public static final String TAG = LetterSortActivity.class.getSimpleName();

    private Context context = LetterSortActivity.this;

    private ClearEditText mClearEditText;
    private TextView tv_mid_letter;
    private ListView listView;
    private MyLetterSortView right_letter;

    private ItemBeanAdapter mAdapter;
    private List<City> mlist = new ArrayList<City>();
    private InputMethodManager inputMethodManager;

    private View mCityContainer;
    private FrameLayout mSearchContainer;
    private ListView mSearchListView;
    private SearchCityAdapter mSearchCityAdapter;
    String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        Intent it = getIntent();
        code = it.getStringExtra("code");
        initView();
        setLinstener();
        initData();
        fillData();
    }

    protected void initData() {

        getDataCity();
        mAdapter = new ItemBeanAdapter(this, mlist);
        listView.setEmptyView(findViewById(R.id.citys_list_load));
        listView.setAdapter(mAdapter);

    }

    protected void initView() {

        inputMethodManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        listView = (ListView) findViewById(R.id.list);
        mClearEditText = (ClearEditText) findViewById(R.id.et_msg_search);
        // 这里设置中间字母
        right_letter = (MyLetterSortView) findViewById(R.id.right_letter);
        tv_mid_letter = (TextView) findViewById(R.id.tv_mid_letter);
        right_letter.setTextView(tv_mid_letter);
        //搜索
        mCityContainer = findViewById(R.id.city_content_container);
        mSearchContainer = (FrameLayout) this.findViewById(R.id.search_content_container);
        mSearchListView = (ListView) findViewById(R.id.search_list);
        mSearchListView.setEmptyView(findViewById(R.id.search_empty));
        mSearchContainer.setVisibility(View.GONE);

    }

    protected void setLinstener() {

        // tv_reget_pwd.setOnClickListener(this);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (code.equals("0")) {
                    Intent intent = new Intent();
                    intent.putExtra("city", ((City) mAdapter.getItem(position)).getCity());
                    setResult(1, intent);
                    finish();
                } else if (code.equals("1")) {
                    Intent intent = new Intent();
                    intent.putExtra("city", ((City) mAdapter.getItem(position)).getCity());
                    setResult(2, intent);
                    finish();
                }


            }
        });

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(
                                getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        // 设置右侧触摸监听
        right_letter
                .setOnTouchingLetterChangedListener(new MyLetterSortView.OnTouchingLetterChangedListener() {

                    @Override
                    public void onTouchingLetterChanged(String s) {
                        // 该字母首次出现的位置
                        int position = mAdapter.getPositionForSection(s
                                .charAt(0));
                        if (position != -1) {
                            listView.setSelection(position);
                        }

                    }
                });

        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData2(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSearchListView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub
                        // L.i(mSearchCityAdapter.getItem(position).toString());
                        //	T.showLong(getApplicationContext(), mSearchCityAdapter
                        //			.getItem(position).toString());
                       /* Intent intent = new Intent();
                        intent.putExtra("city", mSearchCityAdapter
                                .getItem(position).getCity() + "");
                        setResult(1, intent);
                        finish();*/
                        if (code.equals("0")) {
                            Intent intent = new Intent();
                            intent.putExtra("city", mSearchCityAdapter
                                    .getItem(position).getCity() + "");
                            setResult(1, intent);
                            finish();
                        } else if (code.equals("1")) {
                            Intent intent = new Intent();
                            intent.putExtra("city", mSearchCityAdapter
                                    .getItem(position).getCity() + "");
                            setResult(2, intent);
                            finish();
                        }
                    }

                });
    }

    protected void fillData() {
        // TODO Auto-generated method stub

    }

    private void getDataCity() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                MyCityDBHelper myCityDBHelper = new MyCityDBHelper(context);

                mlist = myCityDBHelper.getCityDB().getAllCity();
                mHandler.sendEmptyMessage(0);

            }
        }).start();

        // 对list进行排序
        // Collections.sort(mlist, new PinyinComparator() {
        // });

    }


    private void filterData2(String filterStr) {
        mSearchCityAdapter = new SearchCityAdapter(LetterSortActivity.this,
                mlist);
        mSearchListView.setAdapter(mSearchCityAdapter);
        mSearchListView.setTextFilterEnabled(true);
        if (mlist.size() < 1 || TextUtils.isEmpty(filterStr)) {
            mCityContainer.setVisibility(View.VISIBLE);
            mSearchContainer.setVisibility(View.INVISIBLE);

        } else {

            mCityContainer.setVisibility(View.INVISIBLE);
            mSearchContainer.setVisibility(View.VISIBLE);
            mSearchCityAdapter.getFilter().filter(filterStr);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //
            default:
                break;
        }

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:

                    mAdapter.updateListView(mlist);
                    break;
                default:
                    break;
            }
        }
    };
}
