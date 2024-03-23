package com.and103.asm_and103;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    List<CarModel> listCarModel;
    FloatingActionButton floatadd;

    ListView lvMain;

    CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lvMain = findViewById(R.id.listviewMain);
        getdanhsach();
        floatadd = findViewById(R.id.floatadd);
        floatadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addXe();
            }
        });
    }
    void addXe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_xe, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edName = view.findViewById(R.id.edName);
        EditText edNamSX = view.findViewById(R.id.edNamSX);
        EditText edHang = view.findViewById(R.id.edHang);
        EditText edGia = view.findViewById(R.id.edGia);
        ImageView imgchonanh=view.findViewById(R.id.imgchonanh);
        Button btnthemm = view.findViewById(R.id.btnthem);
        btnthemm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Thêm");
                builder.setMessage("Bạn có muốn thêm Xe không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ten = edName.getText().toString().trim();
                        int nam = Integer.valueOf(edNamSX.getText().toString().trim());
                        String hang = edHang.getText().toString().trim();
                        double gia = Double.valueOf(edGia.getText().toString().trim());
                        // Kiểm tra các trường thông tin có được nhập đầy đủ hay không
                        if (ten.isEmpty() || hang.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Tạo một đối tượng SinhVien mới
                        CarModel car = new CarModel(ten,nam,hang,gia);

                        // Gửi đối tượng SinhVien lên server
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(APIService.DOMAIN)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        APIService apiService = retrofit.create(APIService.class);
                        Call<List<CarModel>> call = apiService.addXe(car);
                        call.enqueue(new Callback<List<CarModel>>() {
                            @Override
                            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                                if (response.isSuccessful()) {
                                    getdanhsach();
                                    Log.e("thanhcong","jdsbfdsgfdg");
                                    dialog.dismiss();

                                } else {
                                    Log.e("Thatbai","aaaaaaaaaaaaaa");
                                }
                            }

                            @Override
                            public void onFailure(Call<java.util.List<CarModel>> call, Throwable t) {
                                getdanhsach();

                                dialog.dismiss();
                            }


                        });
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
    private  void getdanhsach(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        APIService apiService = retrofit.create(APIService.class);

        Call<List<CarModel>> call = apiService.getCars();
        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful()) {
                    listCarModel = response.body();

                    carAdapter = new CarAdapter(getApplicationContext(), listCarModel);

                    lvMain.setAdapter(carAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });
    }

}