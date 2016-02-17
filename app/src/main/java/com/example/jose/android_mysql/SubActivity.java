package com.example.jose.android_mysql;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

public class SubActivity extends AppCompatActivity implements AsyncResponse {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		PostResponseAsyncTask task = new PostResponseAsyncTask(SubActivity.this, this);

		task.execute("http://10.0.3.2:8080/customer/product.php");
	}

	@Override
	public void processFinish(String jsonText) {

		ImageLoader.getInstance().init(UILConfig());

		ArrayList<Product> productList = new JsonConverter<Product>().toArrayList(jsonText,Product.class);
		BindDictionary<Product> dict = new BindDictionary<Product>();
		dict.addStringField(R.id.tvName, new StringExtractor<Product>() {
			@Override
			public String getStringValue(Product product, int position) {
				return product.name;
			}
		});

		dict.addStringField(R.id.tvQty, new StringExtractor<Product>() {
			@Override
			public String getStringValue(Product product, int position) {
				return "" + product.qty;
			}
		});

		dict.addStringField(R.id.tvPrice, new StringExtractor<Product>() {
			@Override
			public String getStringValue(Product product, int position) {
				return "" + product.price;
			}
		});

		dict.addDynamicImageField(R.id.imageView, new StringExtractor<Product>() {
					@Override
					public String getStringValue(Product product, int position) {
						return product.image_url;
					}
				}, new DynamicImageLoader() {
					@Override
					public void loadImage(String url, ImageView view) {

						ImageLoader.getInstance().displayImage(url, view);
						view.setPadding(0, 0, 0, 0);
						view.setAdjustViewBounds(true);

					}
				}
		);

		FunDapter<Product> adapter = new FunDapter<>(SubActivity.this, productList,
				R.layout.product_layout, dict);

		ListView lvProduct = (ListView)findViewById(R.id.IvProduct);
		lvProduct.setAdapter(adapter);

	}

	private ImageLoaderConfiguration UILConfig(){
		DisplayImageOptions defaultOptions =
				new DisplayImageOptions.Builder()
						.cacheInMemory(true)
						.cacheOnDisk(true)
						.showImageOnLoading(android.R.drawable.stat_sys_download)
						.showImageForEmptyUri(android.R.drawable.ic_dialog_alert)
						.showImageOnFail(android.R.drawable.stat_notify_error)
						.considerExifParams(true)
						.bitmapConfig(Bitmap.Config.RGB_565)
						.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
						.build();

		ImageLoaderConfiguration config =
				new ImageLoaderConfiguration
						.Builder(getApplicationContext())
						.threadPriority(Thread.NORM_PRIORITY - 2)
						.denyCacheImageMultipleSizesInMemory()
						.diskCacheFileNameGenerator(new Md5FileNameGenerator())
						.tasksProcessingOrder(QueueProcessingType.LIFO)
						.defaultDisplayImageOptions(defaultOptions)
						.build();

		return config;
	}
}
