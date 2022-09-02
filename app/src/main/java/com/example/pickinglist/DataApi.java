package com.example.pickinglist;

/*  Classe che si occupa dell'interazione con il db ed il gestionale
**/

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class DataApi {
    String webApiClass = "PickingListMobileApiWebApi";
    String authKey = "10P9mU3aMttOeo10elo19dev13per";
    String url = "https://letizia.ngrok.io/api/PickingListMobileApiWebApi/";
    String domine;

    Plant plant;
    final boolean[] isAuthenticated = new boolean[1];
    final ArrayList<Plant>[] plants = new ArrayList[]{new ArrayList<>()};
    Long loggedUserId;
    List<PickingList> pickingList;
    ArrayList<Section> pickingListBySection;
    Integer[] pickingListsShowed;

    RequestQueue queue;
    Context context;

    public DataApi() { }
    public DataApi(Plant plant, Long userId)
    {
        this.plant = plant;
        this.loggedUserId = userId;
    }

    //#region Setter & Getter
    public Plant getPlant() { return plant; }
    public void setPlant(Plant p) { this.plant = p; }

    public Long getLoggedUserId() { return loggedUserId; }
    public void setLoggedUserId(Long loggedUserId) { this.loggedUserId = loggedUserId; }

    public ArrayList<Section> getPickingListBySection() { return this.pickingListBySection; }
    public void setPickingListBySection(ArrayList<Section> pickingListBySection) { this.pickingListBySection = pickingListBySection; }

    public List<PickingList> getPickingLists() { return pickingList; }
    public void setPickingList(ArrayList<PickingList> pickingList) { this.pickingList = pickingList; }

    public Integer[] getPickingListsShowed(){ return this.pickingListsShowed; }
    public void setPickingListsShowed(Integer[] pickingListsShowed){ this.pickingListsShowed = pickingListsShowed; }
    //#endregion

    /** Verifica le credenziali inserite dall'utente
     * @param username: nome utente fornito dall'utente;
     * @param password: password fornita dall'utente;
     * @param context: contesto dell'activity corrente mentre si fa la richiesta;
     * @return true: se le trova tra gli utenti della compagnia indicata;
     * @return false: altrimenti (errore in uno dei parametri).
     * **/
    public void verifyCredential(String username, String password, Context context, VolleyCallback callback)
    {
        //connessione al db e verifica credenziali
        this.context = context;

        try {
            JSONObject object = new JSONObject();
            object.put("AuthKey", this.authKey);
            object.put("username", username);
            object.put("password", password);

            queue = Volley.newRequestQueue(context);
            JsonObjectRequest request = new JsonObjectRequest(url + "VerifyCredential", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (null != response) {
                                try {
                                    boolean e = response.getBoolean("isAuthenticated");
                                    callback.onSuccessResponse(e == true? "t" : "f");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.wtf("2", "onErrorResponse: " + error.getMessage() );
                        }
                    });
            queue.add(request );
        }
        catch (Exception e)
        {
            Log.wtf("2", "getPlants: " + e.getMessage() );
        }
    }

    /** Cerca tutti i plant di una determinata compagnia
     * @param companyName: nome della compagnia;
     * @return lista dei plant;
     * @return null: se non trova i plant (possibile segno di un errore nel nome della compagnia.)
     * **/
    public void getPlants(String companyName, Context context, VolleyCallback callback)
    {
        //connessione al db del cliente e ritorna la lista dei plant

        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JSONObject object = new JSONObject();
            object.put("AuthKey", authKey);

            JsonObjectRequest request = new JsonObjectRequest(url + "GetPlants", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (null != response) {
                                try {
                                    //handle your response
                                    Log.wtf("2", "onResponse: " + response.get("plants"));

                                    callback.onSuccessResponse(response.get("plants").toString());

                                  /*  plants[0] = new ArrayList<Plant>();
                                    JSONArray array = new JSONArray(response.get("plants").toString());
                                    JSONObject obj;
                                    for (int i = 0; i < array.length(); i++) {
                                        obj = new JSONObject(array.getJSONObject(i).toString());
                                        plants[0].add(new Plant(obj.getString("name"), obj.getLong("id")));
                                    }

                                    Log.wtf("2", "onResponse: " + plants[0].toString());*/
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf("2", "onErrorResponse: " + error.getMessage() );
                }
            });
            queue.add(request);
        }
        catch (Exception e)
        {
            Log.wtf("2", "getPlants: " + e.getMessage() );
        }
    }

    /** Carica le picking list nella proprietà "pickingList"
     * @return true: se le carica correttamente;
     * @return false: se non riesce a caricarle.
     * **/
    public boolean loadPickingLists()
    {
        //connessione al db e carica in "pickingList" tutte le pickinglist del plant

        this.pickingList = new ArrayList<PickingList>();

        List<Article> a =  new ArrayList<>();

        a.add(new Article(5, "sca","brufen 300",  234435, 20, new Location(1,3,"B")));
        a.add(new Article(10, "sca","zirtec",  223345, 20, new Location(9,5, "F")));
        PickingList p = new PickingList("reparto1", a);
        this.pickingList.add(p);

        a =  new ArrayList<>();
        a.add( new Article(1, "sca20", "tachipirina 1000", 2345, 20, new Location(25, 7, "Q")));
        a.add(new Article(5, "sca10","oki",  234435, 20, new Location(8,10, "H")));
        a.add(new Article(10, "sca","gaviscon",  223345, 20, new Location(5, 3, "A")));
        p = new PickingList("reparto2", a);
        this.pickingList.add(p);

        a =  new ArrayList<>();
        a.add( new Article(1, "sca50", "tachidol", 2345, 20, new Location(6,1, "C")));
        a.add(new Article(5, "sca","oki task",  234435, 20, new Location(2, 15, "F")));
        a.add(new Article(10, "sca50","moment",  223345, 20, new Location(4, 20, "D")));
        p = new PickingList("reparto3", a);
        this.pickingList.add(p);

        a =  new ArrayList<>();
        a.add( new Article(1, "sca", "buscopan", 2345, 20, new Location(2, 1, "C")));
        a.add(new Article(5, "sca20","brufen 600",  234435, 20, new Location(5,9, "B")));
        a.add(new Article(10, "sca","tachipirina 500",  223345, 20, new Location(4, 22, "A")));
        p = new PickingList("reparto4", a);
        this.pickingList.add(p);

        return true;
    }

    /** Cerca le picking lists di determinati indici e ritorna le sezioni con all'interno gli articoli delle picking lists in input
     * @param positions: indici delle picking lists da considerare;
     * @return lista di sezioni con dentro articoli.
     * **/
    public ArrayList<Section> getPickingListsGroupedBySection(Integer[] positions)
    {
        this.pickingListsShowed = positions;

        if(!this.pickingList.isEmpty() && positions.length != 0)
        {
            ArrayList<Section> sections = new ArrayList<Section>();

            for (Integer p : positions) {
                if(p < this.pickingList.size() && p >= 0)
                {
                    for (Article a : this.pickingList.get(p).articles)
                    {
                        boolean isNewSection = true;
                        for (Section s : sections)
                        {
                            if(s.sectionName == a.GetLocation().getSection())
                            {
                                s.getArticles().add(a);
                                isNewSection = false;
                            }
                        }

                        if(isNewSection)
                        {
                            ArrayList<Article> articles = new ArrayList<Article>(){};
                            articles.add(a);
                            Section newSection = new Section(a.GetLocation().getSection(), articles);
                            sections.add(newSection);
                        }
                    }
                }
            }

            this.pickingListBySection = sections;
            return sections;
        }

        return null;
    }

    /** Ritorna tutti i nomi delle picking lists del plant
     * @return array di stringhe con i nomi contenuti;
     * @return null se non ci sono picking lists;
     * **/
    public String[] getPickingListTitles()
    {
        if(this.pickingList.isEmpty())
            return null;

        String[] titles = new String[this.pickingList.size()];
        for (int i = 0; i < this.pickingList.size(); i++) {
            titles[i] = this.pickingList.get(i).name;
        }

        return titles;
    }

    /** Ritorna tutti i nomi delle picking lists del plant
     * @return array di stringhe con i nomi contenuti;
     * @return null se non ci sono picking lists;
     * **/
    public String[] getPickingListTitlesBySection()
    {
        if(this.pickingListBySection.isEmpty())
            return null;

        String[] titles = new String[this.pickingListBySection.size()];
        for (int i = 0; i < this.pickingListBySection.size(); i++) {
            titles[i] = this.pickingListBySection.get(i).sectionName;
        }

        return titles;
    }

    /** Collegandosi al server elimina una picking list
     * @param  id: identificativo della picking list da eliminare;
     * @return true: se la rimuove correttamente;
     * @return false: se non riesce a rimuoverla.
     * **/
    public boolean removePickingList(Long id)
    {
        //connessione al db e elimina la pickinglist

        return true;
    }

    /** Collegandosi al server modifica una picking list
     * @param id: identificativo della picking list da modificare;
     * @param newArticles: nuovi articoli della picking list;
     * @return true: se la modifica correttamente;
     * @return false: se non riesce a modificarla.
     * **/
    public boolean modifyPickingList(Long id, List<Article> newArticles)
    {
        //connessione al db e elimina la pickinglist

        return true;
    }

    public void getResponseLight(String url, JSONObject jsonValue, final VolleyCallback callback) {

        queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(url, jsonValue,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (null != response) {
                            callback.onSuccessResponse(response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("2", "onErrorResponse: " + error.getMessage() );
            }
        });
        queue.add(request);
    }

  public void getResponse(int method, String url, JSONObject jsonValue, final VolleyCallback callback) {

        queue = MySingleton.getInstance(context).getRequestQueue();

        StringRequest strreq = new StringRequest(Request.Method.GET, url, new Response.Listener < String > () {

            @Override
            public void onResponse(String Response) {
                //callback.onSuccessResponse(Response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toast.makeText(context, e + "error", Toast.LENGTH_LONG).show();
            }
        })
        {
            // set headers
            @Override
            public Map< String, String > getHeaders() throws com.android.volley.AuthFailureError {
                Map < String, String > params = new HashMap< String, String >();
               // params.put("Authorization: Basic", TOKEN);
                params.put("Authorization: Basic", authKey);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(strreq);
    }

    public static class PickingList
    {
        String name;
        Long id;
        List<Article> articles;

        public PickingList(){}
        public PickingList(String name, List<Article> articles)
        {
            this.name = name;
            this.articles = articles;
            this.id = Long.valueOf(0);
        }
    }

    public static class Plant
    {
        String displayName;
        Long id;

        public Plant(){}
        public Plant(String name, Long id)
        {
            this.displayName = name;
            this.id  = id;
        }
    }
}
