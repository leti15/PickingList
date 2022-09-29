package com.example.pickinglist;

/*  Classe che si occupa dell'interazione con il db ed il gestionale
**/

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.dom.DOMLocator;

public class DataApi {
    String webApiClass = "PickingListMobileApiWebApi";
    String authKey = "72AqzERXTBUXkIt$";
    String url = "https://letizia.ngrok.io/api/PickingListMobileApiWebApi/";

    String plantName;
    String domainName;
    Long loggedUserId;
    ArrayList<PickingList> pickingList;
    ArrayList<Section> pickingListBySection;
    ArrayList<Integer> pickingListsShowed;

    RequestQueue queue;
    Context context;

    /*
    Stati PickingList:
        - Aperto
        - Da prelevare
        - In prelievo
        - Prelevato
        - Annullato
*/

    public DataApi()
    {
        this.domainName = "";
    }
    public DataApi(String plant)
    {
        this.plantName = plant;
        this.domainName = "";
    }
    public DataApi(String plant, String domain)
    {
        this.plantName = plant;
        this.domainName = domain.toLowerCase(Locale.ROOT).trim();
        this.url = "https://" + this.domainName + ".vettoreweb.it/PickingListMobileApiWebApi/";
    }

    //#region Setter & Getter
    public String getPlantName() { return plantName; }
    public void setPlantName(String p) { this.plantName = p; }

    public String getDomainName() { return domainName; }
    public void setDomainName(String p) { this.domainName = p; }

    public Long getLoggedUserId() { return loggedUserId; }
    public void setLoggedUserId(Long loggedUserId) { this.loggedUserId = loggedUserId; }

    public ArrayList<Section> getPickingListBySection() { return this.pickingListBySection; }
    public void setPickingListBySection(ArrayList<Section> pickingListBySection) { this.pickingListBySection = pickingListBySection; }

    public ArrayList<PickingList> getPickingLists() { return pickingList; }
    public void setPickingList(ArrayList<PickingList> pickingList) { this.pickingList = pickingList; }

    public ArrayList<Integer> getPickingListsShowed(){ return this.pickingListsShowed; }
    public void setPickingListsShowed(ArrayList<Integer> pickingListsShowed){ this.pickingListsShowed = pickingListsShowed; }
    //#endregion

    //Methods
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

        try
        {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest request;
            JSONObject object = new JSONObject();
            object.put("AuthKey", authKey);

            if(domainName.compareTo("") != 0)
            {
                String url2 = "https://" + this.domainName + ".vettoreweb.it/PickingListMobileApiWebApi/GetPlants";
                request = new JsonObjectRequest(url2, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (null != response) {
                                    try {
                                        callback.onSuccessResponse(response.get("plants").toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("2", "onErrorResponse: " + error.getMessage());
                    }
                });
            }
            else
            {
                request = new JsonObjectRequest(url + "GetPlants", object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (null != response) {
                                    try {
                                        callback.onSuccessResponse(response.get("plants").toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("2", "onErrorResponse: " + error.getMessage());
                    }
                });
            }
            queue.add(request);
        }
        catch (Exception e)
        {
            Log.wtf("2", "getPlants: " + e.getMessage() );
        }
    }

    /** Carica le picking list nella proprietà "pickingList"
     * **/
    public void loadPickingLists(Context context, VolleyCallback callback)
    {
        //connessione al db e carica in "pickingList" tutte le pickinglist del plant

        this.pickingList = new ArrayList<PickingList>();
        this.pickingListsShowed = new ArrayList<>();

        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JSONObject object = new JSONObject();
            object.put("AuthKey", authKey);
            object.put("PlantName", plantName);

            JsonObjectRequest request = new JsonObjectRequest(url + "GetPickingLists", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (null != response) {
                                try {
                                    callback.onSuccessResponse(response.get("pickingLists").toString());
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
            Log.wtf("2", e.getMessage());
        }
    }

    /** Cerca le picking lists di determinati indici e ritorna le sezioni con all'interno gli articoli delle picking lists in input
     * @param positions: indici delle picking lists da considerare;
     * @return lista di sezioni con dentro articoli.
     * **/
    public ArrayList<Section> getPickingListsGroupedBySection(Integer... positions)
    {
        for (Integer p : positions) {
            if(this.pickingListsShowed.contains(p))
                this.pickingListsShowed.remove(p);
            else
                this.pickingListsShowed.add(p);
        }

        ArrayList<Integer> pickingListIndexes = new ArrayList<>();
        if(this.pickingListsShowed.size() == 0)
            for (int i = 0; i < this.pickingList.size(); i++)
                pickingListIndexes.add(i);
        else
            pickingListIndexes = this.pickingListsShowed;

        if(!pickingListIndexes.isEmpty())
        {
            ArrayList<Section> sections = new ArrayList<Section>();

            for (Integer p : pickingListIndexes) {
                if(p < this.pickingList.size() && p >= 0)
                {
                    for (Article a : this.pickingList.get(p).articles)
                    {
                        boolean isNewSection = true;
                        for (Section s : sections)
                        {
                            String sectionName = a.getLocation().getStorageUnit(0);
                            if(s.sectionName.compareTo(sectionName) == 0)
                            {
                                s.getArticles().add(a);
                                isNewSection = false;
                            }
                        }

                        if(isNewSection)
                        {
                            ArrayList<Article> articles = new ArrayList<Article>(){};
                            articles.add(a);
                            Section newSection = new Section(a.getLocation().getStorageUnit(0), articles);
                            sections.add(newSection);
                        }
                    }
                }
            }

           // Section.orderByShelvingUnit(sections);
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

    /** Collegandosi al server salva tutte le pickingLists
     * **/
    public void savePickingLists(Context context)
    {
        //connessione al db e elimina la pickinglist

        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JSONObject object = new JSONObject();
            object.put("AuthKey", authKey);
            object.put("Plant", plantName);
            object.put ("PickingLists", builPickingListForServer());

            JsonObjectRequest request = new JsonObjectRequest(url + "Save", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (null != response) {
                                try {
                                    boolean e = response.getBoolean("success");

                                    if(e)
                                        Toast.makeText(context, "Salvataggio picking lists completato", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(context, "Salvataggio picking lists non andato a buon fine", Toast.LENGTH_SHORT).show();

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
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

    private JSONArray builPickingListForServer() {

        JSONArray pickingListArray = new JSONArray();
        JSONObject pickingListObj;
        JSONArray articlesArray;
        JSONObject articleObj;
        JSONObject locationObj;
        try {
            for (PickingList pickingList: this.pickingList)
            {
                pickingListObj = new JSONObject();
                articlesArray = new JSONArray();

                for (Article article : pickingList.getArticles())
                {
                    articleObj = new JSONObject();
                    locationObj = new JSONObject();

                    Location location = article.getLocation();
                    locationObj.put("id", location.getId());
                    locationObj.put("code", location.getCode());
                    locationObj.put("name", location.getName());
                    locationObj.put("warehouseId", location.getWarehouseId());
                    locationObj.put("plantId", location.getPlantId());
                    locationObj.put("parentId", location.getParentId());

                    articleObj.put("id", article.getId());
                    articleObj.put("qta", article.getQta());
                    articleObj.put("measureUnit", article.getMeasureUnit());
                    articleObj.put("name", article.getName());
                    articleObj.put("registerCode", article.getRegisterCode());
                    articleObj.put("location", location);

                    articlesArray.put(articleObj);
                }

                pickingListObj.put("id", pickingList.getId());
                pickingListObj.put("name", pickingList.getName());
                pickingListObj.put("articles", articlesArray);

                pickingListArray.put(pickingListObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pickingListArray;
    }

    public static class PickingList
    {
        Long id;
        String name;
        List<Article> articles;

        //Constructor
        public PickingList(){}
        public PickingList(long id, String name, List<Article> articles)
        {
            this.name = name;
            this.articles = articles;
            this.id = id;
        }

        //Getters & Setters
        public Long getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public List<Article> getArticles() {  return articles; }
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
