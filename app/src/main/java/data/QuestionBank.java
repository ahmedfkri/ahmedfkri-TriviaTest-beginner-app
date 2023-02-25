package data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import controller.AppController;
import model.Question;

public class QuestionBank {

    ArrayList<Question> questions= new ArrayList<>();
    private String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                , url
                , null
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("JSON","onResponse: "+ response);
                for(int i =0;i<response.length();i++){
                    try {
                        Question question = new Question();
                        question.setAnswer(response.getJSONArray(i).getString(0));
                        question.setAnswerTure(response.getJSONArray(i).getBoolean(1));
                       // Log.d("hello",""+ question);
                        questions.add(question);
                    }
                    catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                if(callBack != null)callBack.processFinished(questions);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);



        return questions;
    }

}
