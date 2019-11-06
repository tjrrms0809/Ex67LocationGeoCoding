package com.ahnsafety.ex67locationgeocoding;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etAddress;
    EditText etLat, etLng;

    double lat1, lng1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAddress= findViewById(R.id.et_address);
        etLat= findViewById(R.id.et_lat);
        etLng= findViewById(R.id.et_lng);

    }

    public void clickBtn(View view) {
        // 주소 --> 좌표 (지오코딩)
        String addr= etAddress.getText().toString();

        //지오코딩 작업을 수행하는 객체 생성
        Geocoder geocoder= new Geocoder(this, Locale.KOREA);

        //지오코더에게 지오코딩작업 요청
        try {
            List<Address> addresses= geocoder.getFromLocationName(addr, 3);

            StringBuffer buffer= new StringBuffer();
            for(Address t : addresses){
                buffer.append( t.getLatitude()+", "+ t.getLongitude()+"\n");
            }

            //대표 좌표값(첫번째 결과)의 위도, 경도
            lat1= addresses.get(0).getLatitude();
            lng1= addresses.get(0).getLongitude();

            //다이얼로그로 좌표들 보여주기
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage(buffer.toString()).setPositiveButton("OK", null).create().show();


        } catch (IOException e) {
            Toast.makeText(this, "검색 실패", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBtn2(View view) {
        // 좌표 --> 주소 (역지오코딩)

        double lat= Double.parseDouble(etLat.getText().toString());
        double lng= Double.parseDouble(etLng.getText().toString());

        Geocoder geocoder= new Geocoder(this, Locale.KOREA);

        try {
            List<Address> addresses= geocoder.getFromLocation(lat, lng, 3);

            StringBuffer buffer= new StringBuffer();
            for(Address t : addresses){
                buffer.append( t.getCountryName()+"\n" ); //나라이름
                buffer.append( t.getPostalCode()+"\n" ); //우편번호
                buffer.append( t.getAddressLine(0)+"\n"); //주소1
                buffer.append( t.getAddressLine(1)+"\n"); //주소2 -없으면 null
                buffer.append( t.getAddressLine(2)+"\n"); //주소3 -없으면 null
                buffer.append("---------\n");
            }

            //다이얼로그로 결과 보여주기
            new AlertDialog.Builder(this).setMessage(buffer.toString()).setPositiveButton("OK", null).create().show();


        } catch (IOException e) {
            Toast.makeText(this, "검색 실패", Toast.LENGTH_SHORT).show();
        }
    }


    public void clickShowMap(View view) {
        //지도 앱을 실행(지도앱이 설치되어 있어야 함)
        //다른 앱을 실행시키는 방법(묵시적 Intent 이용)
        Intent intent= new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        //지도좌표값 데이터 정보
        Uri uri= Uri.parse("geo:"+lat1+","+lng1+"?z=16"+"&q="+lat1+","+lng1);
        intent.setData(uri);

        startActivity(intent);
    }
}
