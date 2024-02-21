package com.example.newsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategorClickInterface{

    // 73a8895c49a945609027d5faad632def

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }

    private void getCategories(){
        categoryRVModelArrayList.add(new CategoryRVModel("All","https://pixabay.com/photos/news-daily-newspaper-press-1172463/"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology","https://assets.thehansindia.com/h-upload/2021/07/31/1092805-tech.webp"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science","https://img.freepik.com/free-vector/youtube-thumbnail-science-research_23-2150228578.jpg?w=1060&t=st=1708272930~exp=1708273530~hmac=d1cf8d4c2a2911b200855abfdcb8e545de1ea6ea9b551dd7d56f73bc1c0faf6e"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sport","https://www.shutterstock.com/image-photo/grand-sports-collage-soccer-basketball-hockey-1071589835"));
        categoryRVModelArrayList.add(new CategoryRVModel("General","https://scontent.fjai3-1.fna.fbcdn.net/v/t39.30808-1/310621667_212933574403181_2740232191726636357_n.png?stp=dst-png_p120x120&_nc_cat=100&ccb=1-7&_nc_sid=4da83f&_nc_ohc=JTPmiM7P3AsAX94sv6k&_nc_oc=AQmGhwS2KHZwm3d_ZRymtShpNbofNyzIRWziYL9Gl8VMNKpoTLXyhKcRK_bdDmTHlA5GUOVdw_gVR39BZTvGCqsL&_nc_ht=scontent.fjai3-1.fna&oh=00_AfDXW6lwnyutgcKRPaJogOaGr7WcDQtTAB-ndSq8jm6I3g&oe=65D6D7B4"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business","data:image/jpeg;base64,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"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment","data:image/jpeg;base64,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"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health","data:image/jpeg;base64,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"));
        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category=" + category +"&apikey=73a8895c49a945609027d5faad632def";
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apikey=73a8895c49a945609027d5faad632def";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;
        if(category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }else{
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();
                for (int i=0 ;i<articles.size();i++ ){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));

                }
                newsRVAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail to get news", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModelArrayList.get(position).getCatagory();
        getNews(category);

    }
}