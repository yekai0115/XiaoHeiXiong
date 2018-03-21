package td.com.xiaoheixiong.activity;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import td.com.xiaoheixiong.R;

public class ChooseBankActivity extends Activity {

	private GridView gv;
	private Button btn;
	private TranslateAnimation taLeft, taRight, taTop, taBlow;
	// private int[] imgList = new int[15];
	private MyAdapter mAdapter;
	private LayoutInflater mInflater;
	private int positions;

	private String[] c;

	private int[] d;

	private String[] b;

	private String tag = "";

	private Animation push_left_in, push_right_in;
	private Animation slide_top_to_bottom, slide_bottom_to_top;

	private TextView choosebank_tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosebank_layout);

		Intent it = getIntent();
		tag = it.getStringExtra("tag");
		this.InitView();
		this.InitAnima();
		this.InitData();
	}

	private void InitAnima() {
		// TODO Auto-generated method stub

		// push_left_in=AnimationUtils.loadAnimation(this, R.anim.push_left_in);
		// push_right_in=AnimationUtils.loadAnimation(this,
		// R.anim.push_right_in);
		// slide_top_to_bottom=AnimationUtils.loadAnimation(this,
		// R.anim.slide_top_to_bottom);
		// slide_bottom_to_top=AnimationUtils.loadAnimation(this,
		// R.anim.slide_bottom_to_top);

		taLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		taRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		taTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		taBlow = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		taLeft.setDuration(1000);
		taRight.setDuration(1000);
		taTop.setDuration(1000);
		taBlow.setDuration(1000);
	}

	private void InitData() {
		// TODO Auto-generated method stub
		Log.e("", "tag = = " + tag);
		if (tag.equals("0")) {//信用卡
			b = new String[] { "农业银行", "北京银行", "中国银行", "建设银行", "光大银行", "兴业银行", "中信银行", "民生银行", "广发银行", "华夏银行", "工商银行",
					"邮政储蓄", "平安银行", "浦发银行", "招商银行", "上海银行" };
			c = new String[] { "ABCCREDIT", "BCCBCREDIT", "BOCCREDIT", "CCBCREDIT", "EVERBRIGHTCREDIT", "CIBCREDIT",
					"ECITICCREDIT", "CMBCCREDIT", "GDBCREDIT", "HXBCREDIT", "ICBCCREDIT", "PSBCCREDIT", "PINGANCREDIT",
					"SPDBCREDIT", "CMBCHINACREDIT", "BOSHCREDIT" };
			d = new int[] { R.mipmap.ps_abc, R.mipmap.ps_bjb, R.mipmap.ps_boc, R.mipmap.ps_ccb,
					R.mipmap.ps_cebb, R.mipmap.ps_cib, R.mipmap.ps_citic, R.mipmap.ps_cmbc, R.mipmap.ps_gdb,
					R.mipmap.ps_hxb, R.mipmap.ps_icbc, R.mipmap.ps_psbc, R.mipmap.ps_spa, R.mipmap.ps_spdb,
					R.mipmap.ps_cmb, R.mipmap.ps_sh };
		} else if (tag.equals("3")) {
			b = new String[] { "农业银行", "北京银行", "建设银行", "招商银行", "深发银行", "工商银行", "交通银行", "兴业银行", "中国银行", "民生银行" };
			c = new String[] { "", "", "", "", "", "", "", "", "", "" };
			d = new int[] { R.mipmap.ps_abc, R.mipmap.ps_bjb, R.mipmap.ps_ccb, R.mipmap.ps_cmb, R.mipmap.sf,
					R.mipmap.ps_icbc, R.mipmap.ps_comm, R.mipmap.ps_cib, R.mipmap.ps_boc, R.mipmap.ps_cmbc };
			choosebank_tv.setVisibility(View.GONE);
			// choosebank_tv.setText("出款银行卡仅支持以上银行");
		} else if (tag.equals("2")) {

			b = new String[] { "农业银行", "招商银行", "中国银行", "建设银行", "光大银行", "兴业银行", "中信银行", "广东发展银行", "工商银行", "邮政储蓄银行",
					"平安银行", "浦东发展银行", "交通银行" };
			c = new String[] { "ABCCREDIT", "CMBCHINACREDIT", "BOCCREDIT", "CCBCREDIT", "EVERBRIGHTCREDIT", "CIBCREDIT",
					"ECITICCREDIT", "GDBCREDIT", "ICBCCREDIT", "PSBCCREDIT", "PINGANCREDIT", "SDBCREDIT",
					"BOCOCREDIT" };
			d = new int[] { R.mipmap.ps_abc, R.mipmap.ps_cmb, R.mipmap.ps_boc, R.mipmap.ps_ccb,
					R.mipmap.ps_cebb, R.mipmap.ps_cib, R.mipmap.ps_citic, R.mipmap.ps_gdb, R.mipmap.ps_icbc,
					R.mipmap.ps_psbc, R.mipmap.ps_spa, R.mipmap.ps_spdb, R.mipmap.ps_comm };
			choosebank_tv.setVisibility(View.GONE);

		}else if(tag.equals("1")) {
			b = new String[] { "农业银行", "北京银行", "中国银行", "建设银行", "光大银行", "兴业银行", "中信银行", "民生银行", "广发银行", "华夏银行", "工商银行",
					"邮政储蓄", "平安银行", "浦发银行", "招商银行", "上海银行","交通银行" };
			c = new String[] { "ABCCREDIT", "BCCBCREDIT", "BOCCREDIT", "CCBCREDIT", "EVERBRIGHTCREDIT", "CIBCREDIT",
					"ECITICCREDIT", "CMBCCREDIT", "GDBCREDIT", "HXBCREDIT", "ICBCCREDIT", "PSBCCREDIT", "PINGANCREDIT",
					"SPDBCREDIT", "CMBCHINACREDIT", "BOSHCREDIT", "BOCOCREDIT"};
			d = new int[] { R.mipmap.ps_abc, R.mipmap.ps_bjb, R.mipmap.ps_boc, R.mipmap.ps_ccb,
					R.mipmap.ps_cebb, R.mipmap.ps_cib, R.mipmap.ps_citic, R.mipmap.ps_cmbc, R.mipmap.ps_gdb,
					R.mipmap.ps_hxb, R.mipmap.ps_icbc, R.mipmap.ps_psbc, R.mipmap.ps_spa, R.mipmap.ps_spdb,
					R.mipmap.ps_cmb, R.mipmap.ps_sh,R.mipmap.ps_comm  };
		}
		else {
			b = new String[] { "农业银行", "招商银行", "中国银行", "建设银行", "光大银行", "兴业银行", "中信银行", "广东发展银行", "工商银行", "邮政储蓄银行",
					"平安银行", "浦东发展银行", "交通银行" };
			c = new String[] { "ABCCREDIT", "CMBCHINACREDIT", "BOCCREDIT", "CCBCREDIT", "EVERBRIGHTCREDIT", "CIBCREDIT",
					"ECITICCREDIT", "GDBCREDIT", "ICBCCREDIT", "PSBCCREDIT", "PINGANCREDIT", "SDBCREDIT",
					"BOCOCREDIT" };
			d = new int[] { R.mipmap.ps_abc, R.mipmap.ps_cmb, R.mipmap.ps_boc, R.mipmap.ps_ccb,
					R.mipmap.ps_cebb, R.mipmap.ps_cib, R.mipmap.ps_citic, R.mipmap.ps_gdb, R.mipmap.ps_icbc,
					R.mipmap.ps_psbc, R.mipmap.ps_spa, R.mipmap.ps_spdb, R.mipmap.ps_comm };
			choosebank_tv.setVisibility(View.GONE);
		}

		// for (int i = 0; i < 15; i++) {
		// imgList[i] = R.mipmap.ic_launcher;
		// }
		mAdapter = new MyAdapter();
		gv.setAdapter(mAdapter);
	}

	private void InitView() {
		// TODO Auto-generated method stub
		gv = (GridView) findViewById(R.id.gridView1);
		choosebank_tv = (TextView) findViewById(R.id.choosebank_tv);
		// btn = (Button) findViewById(R.id.button1);
		// btn.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// mAdapter = null;
		// mAdapter = new MyAdapter();
		// gv.setAdapter(mAdapter);
		// mAdapter.notifyDataSetChanged();
		// }
		// });
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				// 把返回数据存入Intent

				if (tag.equals("3")) {
					intent.putExtra("result", b[(int) id]);
					setResult(2, intent);
				} else if (tag.equals("2")) {
					intent.putExtra("bankname", b[(int) id]);
					intent.putExtra("banknum", c[(int) id]);
					intent.putExtra("imgid", d[(int) id]);
					setResult(1, intent);

				} else if (tag.equals("0")) {
					intent.putExtra("bankname", b[(int) id]);
					intent.putExtra("banknum", c[(int) id]);
					intent.putExtra("imgid", d[(int) id]);
					setResult(1, intent);

				}else if (tag.equals("1")) {
					intent.putExtra("bankname", b[(int) id]);
					intent.putExtra("banknum", c[(int) id]);
					intent.putExtra("imgid", d[(int) id]);
					setResult(3, intent);
				}

				else {

					intent.putExtra("result", (int) id);
					setResult(1, intent);
					Log.e("", "   == = = = =");
				}

				finish();
			}
		});
	}

	private class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return b.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return b[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			positions = position;
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item, null);
				holder = new ViewHolder();
				holder.textview1 = (TextView) convertView.findViewById(R.id.textview1);
				holder.image = (ImageView) convertView.findViewById(R.id.imageView1);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			int imgID = d[position];
			String text = b[position];
			holder.image.setImageResource(imgID);
			holder.textview1.setText(text);

			Random ran = new Random();
			int rand = ran.nextInt(4);
			switch (rand) {
			case 0:
				convertView.startAnimation(taLeft);
				break;
			case 1:
				convertView.startAnimation(taRight);
				break;
			case 2:
				convertView.startAnimation(taTop);
				break;
			case 3:
				convertView.startAnimation(taBlow);
				break;
			}
			return convertView;
		}

		class ViewHolder {
			public TextView textview1;
			public ImageView image;
		}
	}

}
