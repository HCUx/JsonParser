package com.mobilhanem.jsonparser;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.app.ProgressDialog;

public class MainActivity extends Activity {
	Spinner sp;
	int secilen;
	TextView t1,t2,t3,t4;
	String url = "http://www.mobilhanem.com/test/jsondeneme.php";
	String veri_string;
	PostClass post = new PostClass();  //Post Class dan post adında nesne olusturduk.Post classın içindeki methodu kullanabilmek için

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//activity_main.xml de oluşturduğumuz textview leri koda tanıtıyoruz.
		t1 = (TextView)findViewById(R.id.textView2);
		t2 = (TextView)findViewById(R.id.textView4);
		t3 = (TextView)findViewById(R.id.textView6);
		t4 = (TextView)findViewById(R.id.textView8);

		//activity_main.xml de oluşturduğumuz spineri  koda tanıtıyoruz.
		sp = (Spinner) findViewById(R.id.spinner1);
		
		//Spinera Listener ekliyoruz
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				secilen = arg2; //spinerdan secilen değerin sıra değerini alıyoruz. kaçıncı sırada olduğunu
				if(secilen!=0){ //seçilen deger ilk deger değilse yani Kişi seçiniz yazısı değilse
					new KisiBilgiGetir().execute();//Asynctask classı ıaıırıyoruz.
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	class KisiBilgiGetir extends AsyncTask<Void, Void, Void> {
		ProgressDialog pDialog;
		JSONObject veri_json; //JSONObject sınıfından veri_json adında obje tanımlıyoruz.
		protected void onPreExecute() { // Post tan önce yapılacak işlemler. ProgressDialog gösterdik.
 	        pDialog = new ProgressDialog(MainActivity.this);
	        pDialog.setMessage("Kişi Bilgileri Getiriliyor...");
	        pDialog.setIndeterminate(true);
	        pDialog.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
	        pDialog.show();
	    }
	 
	    protected Void doInBackground(Void... unused) { // Arka Planda yapılacaklar. Yani Post işlemi  
	    	
            List<NameValuePair> params = new ArrayList<NameValuePair>(); //Post edilecek deıiıkenleri ayarliyoruz.
            params.add(new BasicNameValuePair("kisi", ""+secilen));
    		
            veri_string = post.httpPost(url,"POST",params,20000); //PostClass daki httpPost metodunu çağırdık.Gelen string değerini aldık
           
            try {
				veri_json =new JSONObject(veri_string);//gelen veri_string değerini json arraye çeviriyoruz.
				//try içinde yapmak zorunlu çünkü çıkabilecek bir sorunda uygulamanın patlamaması için
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            		
            Log.d("HTTP POST CEVAP:",""+veri_json);// gelen veriyi log tuttuk
            
            return null;     	
	    }       
	 
	    protected void onPostExecute(Void unused) { //Posttan sonra
	    	pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
	    	runOnUiThread(new Runnable() { //Asynctask class içinde arayüzde değişiklik yapmak istiyorsak thread kullanmak zorundayız
				public void run() { 
	            	String isim,yas,mail,adres;
	            	try {
	    				//try içinde yapmak zorunlu çünkü çıkabilecek bir sorunda uygulamanın patlamaması için.
	            		//veri_json arrayindeki deıerleri alıyoruz.
						isim = veri_json.getString("isim");
						yas = veri_json.getString("yas");
						mail = veri_json.getString("mail");
						adres = veri_json.getString("adres");
						
						//bu aldığımız değerleri textView lere yazdırıyoruz.
						t1.setText(isim);
						t2.setText(yas);
						t3.setText(mail);
						t4.setText(adres);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
            });
	    }
	}
	

}
