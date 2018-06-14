1 - instalar dependências 

http://square.github.io/retrofit/

https://github.com/square/retrofit
https://github.com/google/gson
https://github.com/square/retrofit/tree/master/retrofit-converters/gson

2 - criar retrofit instance class
Singleton

public class RetrofitClientInstace {

   private static Retrofit retrofit;
   private static final String BASE_URL ="https://api.github.com/";

   public static Retrofit getRetrofitInstance(){
       if( retrofit == null ){
           retrofit = new Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
       }

       return retrofit;
   }
}

3 - criar model Repo e Definir Endpoints

public interface GitHubService {

   @GET("users/{user}/repos")
   Call<List<Repo>> listRepos(@Path("user") String userName);
}

4 - inserir permissão de acesso à internet

5 - create RepoTask 
public class RepoAsyncTask extends AsyncTask<Void,Void,Void> {

   GitHubService api;
   Call<List<Repo>> call;
   List<Repo> myRepos;

   public RepoAsyncTask(String userName){
       api = RetrofitClientInstace.getRetrofitInstance().create(GitHubService.class);
       call = api.listRepos(userName);

   }
   @Override
   protected Void doInBackground(Void... aVoid) {

       try {
           Response<List<Repo>> response = call.execute();
           if(response.errorBody() == null){
               myRepos = response.body();
           }
       }catch(Exception e){
           e.printStackTrace();
       }

       return null;
   }


   @Override
   protected void onPostExecute(Void aVoid) {
       if(myRepos != null) {
           for (Repo item : myRepos) {
               Log.d("myrepos", "id: " + item.getId() + " name: " + item.getName());
           }
       }

   }
}




6 -criar layout list item
7- criar adaptper
public class MyAdapter extends ArrayAdapter<Repo> {

   Activity context;
   int resource;
   List<Repo> repos;

   public MyAdapter(Activity ctx, List<Repo> objects ){
       this(ctx,R.layout.my_simple_list_item,objects);

       this.context = ctx;
       resource = R.layout.my_simple_list_item;
       repos = objects;
   }

   public MyAdapter(@NonNull Context context, int resource, @NonNull List<Repo> objects) {
       super(context, resource, objects);
   }

   @NonNull
   @Override
   public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View row = null;

       if(convertView == null){
           row = context.getLayoutInflater().inflate(resource,parent,false);

       }

       Repo item = repos.get(position);
       TextView tvName = row.findViewById(R.id.tvName);

       tvName.setText(item.getName());

       return row;



   }
}


8- refatorar asynktask
public class RepoAsyncTask extends AsyncTask<Void,Void,Void> {

   GitHubService api;
   Call<List<Repo>> call;
   List<Repo> myRepos;
   Activity ctx;
   ListView listView;

   public RepoAsyncTask(Activity ctx,String userName, ListView listView){
       api = RetrofitClientInstace.getRetrofitInstance().create(GitHubService.class);
       call = api.listRepos(userName);
       this.listView = listView;
       this.ctx = ctx;

   }
   @Override
   protected Void doInBackground(Void... aVoid) {

       try {
           Response<List<Repo>> response = call.execute();
           if(response.errorBody() == null){
               myRepos = response.body();
           }
       }catch(Exception e){
           e.printStackTrace();
       }

       return null;
   }


   @Override
   protected void onPostExecute(Void aVoid) {
       if(myRepos != null) {

//            for (Repo item : myRepos) {
//                Log.d("myrepos", "id: " + item.getId() + " name: " + item.getName());
//            }

           MyAdapter adapter = new MyAdapter(ctx,myRepos);
           listView.setAdapter(adapter);

       }

   }
}

9 - alterar main activity
public class MainActivity extends AppCompatActivity {

   ListView listView;
   MyAdapter adapter;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       listView = findViewById(R.id.listView);

       RepoAsyncTask task = new RepoAsyncTask(this,"manelv8",listView);
       task.execute();

   }
}


