package tech.sobre.aularetrofit;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RepoAsyncTask extends AsyncTask<Void,Void,Void> {

    GitHubService api;
    Call<List<Repo>> call;
    List<Repo> myRepos;
    Activity ctx;
    ListView listView;

    public RepoAsyncTask(Activity ctx, ListView listView, String userName){
        api = RetrofitInstanceClient.getRetrofitInstance().create(GitHubService.class);
        call = api.listRepos(userName);
        this.ctx = ctx;
        this.listView = listView;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Response<List<Repo>> response = call.execute();
            if(response.errorBody() == null){
                myRepos = response.body();
            }
        }catch (Exception e){

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

//        for(Repo item : myRepos){
//            Log.d("myrepos", "id: "+item.id + " name: "+ item.name);
//        }

        MyAdapter adapter = new MyAdapter(ctx,myRepos);

        listView.setAdapter(adapter);

    }
}
