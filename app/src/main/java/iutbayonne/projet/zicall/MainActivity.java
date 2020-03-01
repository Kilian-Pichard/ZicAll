package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void accederApprentissagePiano(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), ApprentissagePiano.class);
        startActivity(otherActivity);
    }

    public void accederApprentissageGuitare(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), ApprentissageGuitare.class);
        startActivity(otherActivity);
    }

    public void accederAccordeurGuitare(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), AccordeurGuitare.class);
        startActivity(otherActivity);
    }

    public void accederEcriturePartition(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), EcriturePartition.class);
        startActivity(otherActivity);
    }
}