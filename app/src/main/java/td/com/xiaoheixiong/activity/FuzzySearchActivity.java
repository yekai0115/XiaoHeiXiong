package td.com.xiaoheixiong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import td.com.xiaoheixiong.R;
import td.com.xiaoheixiong.Utils.CharacterParser;
import td.com.xiaoheixiong.adapter.SortAdapter;
import td.com.xiaoheixiong.views.ClearEditText;

public class FuzzySearchActivity extends BaseActivity {
	private ListView sortListView;
	private TextView dialog;
	private ClearEditText mClearEditText;
	private ArrayList<String> branchNameList;
	private ArrayList<HashMap<String, Object>> BranchList;
	private String json;
	private SortAdapter adapter;
	private ImageView back_img;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	// private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fuzzysearch);
		Intent intent = getIntent();
		json = intent.getStringExtra("json");
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		// getBranchdata();
		initViews();
	}

	private void json() {
		// TODO Auto-generated method stub

		if (!json.equals("")) {

			JSONObject jsonObj = JSONObject.parseObject(json);
			String bankBranch = jsonObj.getString("bankBranch");
			Log.e("bankBranch", "bankBranch:" + bankBranch);
			List<Map> lists = new ArrayList<Map>();
			branchNameList = new ArrayList<String>();
			lists = JSONObject.parseArray(bankBranch, Map.class);
			Log.e("list", "list" + lists+"");
			// BranchList.addAll((Collection<? extends HashMap<String, Object>>)
			// lists);
			if (lists != null) {
				for (int i = 0; i < lists.size(); i++) {
					branchNameList.add(lists.get(i).get("bankbranchname").toString());

					// handler.sendEmptyMessage(1);
					// initViews();
				}
				Log.e("bankbranchname", branchNameList.toString());
			}
		}

	}

	private void initViews() {
		back_img = (ImageView) findViewById(R.id.back_img);
		back_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(FuzzySearchActivity.RESULT_CANCELED, intent);
				finish();
			}
		});

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				// Toast.makeText(getApplication(),
				// adapter.getItem(position).toString(),
				// Toast.LENGTH_SHORT).show();

				Intent intent = new Intent();
				intent.putExtra("bankname", adapter.getItem(position).toString());
				setResult(6, intent);
				finish();
			}
		});

		// SourceDateList =
		// filledData(getResources().getStringArray(R.array.date));
		json();
		adapter = new SortAdapter(this, branchNameList);
		sortListView.setAdapter(adapter);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				Log.e("s", "s" + s.toString());
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<String> filterDateList = new ArrayList<String>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = branchNameList;
			Log.e("jin", "空值");
		} else {
			filterDateList.clear();

			/*
			 * for (int i = 0; i < branchNameList.size(); i++) { if
			 * (branchNameList.get(i).indexOf(filterStr.toString()) != -1 ||
			 * characterParser.getSelling(branchNameList.get(i)).startsWith(
			 * filterStr.toString())) {
			 * filterDateList.add(branchNameList.get(i)); } }
			 */

			for (String branchName : branchNameList) {

				if (branchName.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(branchName).startsWith(filterStr.toString())) {
					filterDateList.add(branchName);
				}

			}

			/*
			 * for (String string : branchNameList) { String name =
			 * sortModel.getName(); if (name.indexOf(filterStr.toString()) != -1
			 * ||
			 * characterParser.getSelling(name).startsWith(filterStr.toString())
			 * ) { filterDateList.add(sortModel); } }
			 */
		}

		// 根据a-z进行排序
		// Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	/*
	 * Handler handler = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) { // TODO Auto-generated
	 * method stub super.handleMessage(msg);
	 * 
	 * switch (msg.what) { case 1: initViews();
	 * 
	 * break;
	 * 
	 * default: break; }
	 * 
	 * } };
	 */
}
