package tamara.mosis.elfak.walkhike.modeldata;

import android.security.keystore.UserPresenceUnavailableException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoresData {

    private ArrayList<Scores> Scores;
    private HashMap<String, Integer> ScoresMapping;
    public DatabaseReference db;
    private static final String FIREBASE_CHILD= "Scores";

    private ScoresData()
    {
        Scores = new ArrayList<>();
        ScoresMapping  = new HashMap<String, Integer>();

        db = FirebaseDatabase.getInstance().getReference();
        db.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        db.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);

    }

    private static class SingletonHolder {
        public static final ScoresData instance = new ScoresData();
    }

    public static ScoresData getInstance() {
        return ScoresData.SingletonHolder.instance;
    }

    public ArrayList<Scores> getScores() {
        return Scores;
    }

    ScoresData.ListUpdatedEventListener updateListener;
    public void setEventListener(ScoresData.ListUpdatedEventListener listener) {
        updateListener = listener;
    }
    public interface ListUpdatedEventListener {
        void onScoreUpdated(Scores ss);
    }

    ReadyEventListener probaList;
    public void setReadyList(ReadyEventListener listener) {
        probaList = listener;
    }
    public interface ReadyEventListener {
        void onReady();
    }

    ValueEventListener parentEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //if (updateListener != null)
               // updateListener.onScoreUpdated();

            if(probaList != null)
                probaList.onReady();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myScoreKey = dataSnapshot.getKey();

            if (!ScoresMapping.containsKey(myScoreKey)) {
                Scores myScore = dataSnapshot.getValue(Scores.class);
                myScore.key = myScoreKey;
                Scores.add(myScore);
                ScoresMapping.put(myScoreKey, Scores.size() - 1);
                //if (updateListener != null)
                  //  updateListener.onScoreUpdated();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myScoreKey = dataSnapshot.getKey();
            Scores myScore = dataSnapshot.getValue(Scores.class);
            myScore.key = myScoreKey;
            if (ScoresMapping.containsKey(myScoreKey)) {
                int index = ScoresMapping.get(myScoreKey);
                Scores.set(index, myScore);
            } else {
                Scores.add(myScore);
                ScoresMapping.put(myScoreKey, Scores.size() - 1);
            }
            if (updateListener != null)
                updateListener.onScoreUpdated(myScore);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String myScoreKey = dataSnapshot.getKey();
            if (ScoresMapping.containsKey(myScoreKey)) {
                int index = ScoresMapping.get(myScoreKey);
                Scores.remove(index);
                recreateKeyIndexMapping();
            }
           // if (updateListener != null)
               // updateListener.onScoreUpdated();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void AddScore(Scores p)
    {
        String key = db.push().getKey();
        Scores.add(p);
        ScoresMapping.put(key, Scores.size() - 1);
        db.child(FIREBASE_CHILD).child(key).setValue(p);
        p.key = key;
    }

    public Scores getScore(int index) {
        return Scores.get(index);
    }

    public Scores getScore(String userEmail) {
        Scores uu = null;
        for(int i =0; i< this.Scores.size(); i++)
        {
            if(this.Scores.get(i).useer.compareTo(userEmail) ==0)
            {
                uu = this.Scores.get(i);
            }
        }

        return uu;


    }

    public void deleteScore(int index) {

        db.child(FIREBASE_CHILD).child(Scores.get(index).key).removeValue();
        Scores.remove(index);
        recreateKeyIndexMapping();
    }

    public void updateScore(int index,  Scores u)
    {
        Scores uu =Scores.get(index);
        uu.weeklyActivity=u.weeklyActivity;
        uu.monthlyActivity=u.monthlyActivity;
        uu.alltimeActivity = u.alltimeActivity;

        uu.weeklyDistance=u.weeklyDistance;
        uu.monthlyDistance=u.monthlyDistance;
        uu.alltimeDistance = u.alltimeDistance;
        uu.useer = u.useer;

        db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);

    }

    public void updateScoreActivity(int additionalScoreActivity, User u)
    {
        Scores uu = new Scores();
        for(int i =0; i< this.Scores.size(); i++)
        {
            if(this.Scores.get(i).useer.compareTo(u.email) ==0)
            {
                uu = this.Scores.get(i);
            }
        }



        uu.weeklyActivity +=additionalScoreActivity;
        uu.monthlyActivity +=additionalScoreActivity;
        uu.alltimeActivity +=additionalScoreActivity;


        db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);

    }

    public void updateScoreActivity(int additionalScoreActivity, String username)
    {
        Scores uu = new Scores();
        for(int i =0; i< this.Scores.size(); i++)
        {
            if(this.Scores.get(i).useer.compareTo(username) ==0)
            {
                uu = this.Scores.get(i);
                break;
            }
        }



        uu.weeklyActivity +=additionalScoreActivity;
        uu.monthlyActivity +=additionalScoreActivity;
        uu.alltimeActivity +=additionalScoreActivity;


        db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);

    }

    public void updateScoresDates()
    {
        int additionalScoreDistance = 250;
        Scores uu =null;
        for(int i =0; i< this.Scores.size(); i++)
        {
            uu = this.Scores.get(i);
            uu.weeklyDistance += additionalScoreDistance;
            uu.monthlyDistance += additionalScoreDistance;
            uu.alltimeDistance += additionalScoreDistance;
            uu.weeklyActivity +=additionalScoreDistance;
            uu.monthlyActivity +=additionalScoreDistance;
            uu.alltimeActivity +=additionalScoreDistance;
            uu.datetimeWeek = "02022020000000";
            uu.datetimeMonth = "02022020000000";
            db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);

        }
    }

    public void updateResetScoresWeekly(String date)
    {
        Scores uu =null;
        for(int i =0; i< this.Scores.size(); i++)
        {
            uu = this.Scores.get(i);
            uu.weeklyActivity =0;
            uu.weeklyDistance = 0;
            uu.datetimeWeek = date;
            db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);

        }

    }

    public void updateResetScoresMonthly(String date, boolean resetDateTime)
    {
        if (resetDateTime) {
            Scores uu =null;
            for(int i =0; i< this.Scores.size(); i++)
            {
                uu = this.Scores.get(i);
                uu.monthlyActivity =0;
                uu.monthlyDistance = 0;
                uu.datetimeMonth = date;
                db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);
            }
        } else {
            Scores uu =null;
            for(int i =0; i< this.Scores.size(); i++)
            {
                uu = this.Scores.get(i);
                uu.datetimeMonth = date;
                db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);
            }
        }

    }

    public void updateScoreDistance(int additionalScoreDistance, User u)
    {
        Scores uu = null;
        for(int i =0; i< this.Scores.size(); i++)
        {
            if(this.Scores.get(i).useer.compareTo(u.email) ==0)
            {
                uu = this.Scores.get(i);
            }
        }


        if(uu != null) {
            uu.weeklyDistance += additionalScoreDistance;
            uu.monthlyDistance += additionalScoreDistance;
            uu.alltimeDistance += additionalScoreDistance;



            db.child(FIREBASE_CHILD).child(uu.key).setValue(uu);
        }

    }


    private void recreateKeyIndexMapping()
    {
        ScoresMapping.clear();
        for (int i=0;i<Scores.size();i++)
            ScoresMapping.put(Scores.get(i).key,i);
    }
}
