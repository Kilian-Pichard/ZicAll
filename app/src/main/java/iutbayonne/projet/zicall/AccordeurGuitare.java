package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AccordeurGuitare extends AppCompatActivity {

    private TextView frequenceReference;
    private TextView frequenceMesuree;
    private Button mi;
    private Button la;
    private Button re;
    private Button sol;
    private Button si;
    private Button mi2;
    private Button testOk;
    private Button testPasOk;

    private String cordeCourante;

    private String frequenceTest;
    private String frequenceTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accordeur_guitare);


        this.frequenceReference = findViewById(R.id.frequenceReference);
        this.frequenceMesuree = findViewById(R.id.frequenceMesure);

        this.mi = findViewById(R.id.mi);
        this.la = findViewById(R.id.la);
        this.re = findViewById(R.id.re);
        this.sol = findViewById(R.id.sol);
        this.si = findViewById(R.id.si);
        this.mi2 = findViewById(R.id.mi2);

        this.testOk = findViewById(R.id.testOk);
        this.testPasOk = findViewById(R.id.testPasOk);

        this.cordeCourante = "mi";
        this.frequenceTest2 = "268,7";
        this.frequenceTest = "";
        selectionnerCorde(mi);
        mi.setEnabled(false);


        mi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(false);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(true);
                frequenceMesuree.setText("");
                frequenceReference.setText("82.4");

                cordeCourante = "mi";
                selectionnerCorde(mi);
            }
        });

        la.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(true);
                la.setEnabled(false);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(true);
                frequenceMesuree.setText("");
                frequenceReference.setText("110.0");

                cordeCourante = "la";
                selectionnerCorde(la);
            }
        });

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(false);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(true);
                frequenceMesuree.setText("");
                frequenceReference.setText("146.8");

                cordeCourante = "re";
                selectionnerCorde(re);
            }
        });

        sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(false);
                si.setEnabled(true);
                mi2.setEnabled(true);
                frequenceMesuree.setText("");
                frequenceReference.setText("196");

                cordeCourante = "sol";
                selectionnerCorde(sol);
            }
        });

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(false);
                mi2.setEnabled(true);
                frequenceMesuree.setText("");
                frequenceReference.setText("246.9");

                cordeCourante = "si";
                selectionnerCorde(si);
            }
        });

        mi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(false);

                frequenceMesuree.setText("");

                frequenceReference.setText("329.6");

                cordeCourante = "mi2";
                selectionnerCorde(mi2);
            }
        });
    }

    public void selectionnerCorde(View view) {
        switch (cordeCourante){
            case "mi":
                frequenceTest = "82.4";
                break;
            case "la":
                frequenceTest = "110.0";
                break;
            case "re":
                frequenceTest = "146.8";
                break;
            case "sol":
                frequenceTest = "196";
                break;
            case "si":
                frequenceTest = "246.9";
                break;
            case "mi2":
                frequenceTest = "329.6";
                break;
        }
        //Toast.makeText(getApplicationContext(), String.valueOf(cordeCourante), Toast.LENGTH_SHORT).show();
    }


    public void testOk(View view) {
        frequenceMesuree.setTextColor(getResources().getColor(R.color.vert));
        frequenceMesuree.setText(frequenceTest);
    }

    public void testPasOk(View view) {
        frequenceMesuree.setTextColor(getResources().getColor(R.color.rouge));
        frequenceMesuree.setText(frequenceTest2);
    }
}
