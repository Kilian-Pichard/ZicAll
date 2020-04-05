package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Informations extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Informations</font>"));

        TextView paragrapheDesc = findViewById(R.id.paragraphe_desc);
        paragrapheDesc.setMovementMethod(LinkMovementMethod.getInstance());
        enleverSoulignementDesLiens(paragrapheDesc);

        TextView lienGitHub = findViewById(R.id.lien_GitHub);
        lienGitHub.setMovementMethod(LinkMovementMethod.getInstance());
        enleverSoulignementDesLiens(lienGitHub);

        TextView lienTarsosDSP = findViewById(R.id.lien_TarsosDSP);
        lienTarsosDSP.setMovementMethod(LinkMovementMethod.getInstance());
        enleverSoulignementDesLiens(lienTarsosDSP);

        TextView paragrapheTarsosDSP = findViewById(R.id.paragraphe_TarsosDSP);
        paragrapheTarsosDSP.setMovementMethod(LinkMovementMethod.getInstance());
        enleverSoulignementDesLiens(paragrapheTarsosDSP);

        TextView lienFlaticon = findViewById(R.id.lien_Flaticon);
        lienFlaticon.setMovementMethod(LinkMovementMethod.getInstance());
        enleverSoulignementDesLiens(lienFlaticon);

        TextView paragrapheFlaticon = findViewById(R.id.paragraphe_Flaticon);
        paragrapheFlaticon.setMovementMethod(LinkMovementMethod.getInstance());
        enleverSoulignementDesLiens(paragrapheFlaticon);

        TextView creditsFlaticon1 = findViewById(R.id.credits_Flaticon_1);
        creditsFlaticon1.setMovementMethod(LinkMovementMethod.getInstance());
        enleverSoulignementDesLiens(creditsFlaticon1);

        TextView creditsFlaticon2 = findViewById(R.id.credits_Flaticon_2);
        creditsFlaticon2.setMovementMethod(LinkMovementMethod.getInstance());
        enleverSoulignementDesLiens(creditsFlaticon2);

        TextView creditsFlaticon3 = findViewById(R.id.credits_Flaticon_3);
        creditsFlaticon3.setMovementMethod(LinkMovementMethod.getInstance());
        enleverSoulignementDesLiens(creditsFlaticon3);

        TextView creditsFlaticon4 = findViewById(R.id.credits_Flaticon_4);
        creditsFlaticon2.setMovementMethod(LinkMovementMethod.getInstance());
        enleverSoulignementDesLiens(creditsFlaticon4);
    }

    /**
     * Récupère l'interface correspondante à la toolbar désirée et l'affiche.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_touteslesactivites, menu);
        return true;
    }

    /**
     * Associe chaque bouton de la toolbar à une action.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                accederAccueil();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void accederAccueil()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), MainActivity.class);
        // Vide la pile des activity
        otherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(otherActivity);
        finish();
    }

    private void enleverSoulignementDesLiens(TextView textView)
    {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);

        for (URLSpan span: spans)
        {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }

        textView.setText(s);
    }

    /**
     * Classe nécessaire pour enlever le style des liens contenus dans des TextView.
     */
    private class URLSpanNoUnderline extends URLSpan
    {
        public URLSpanNoUnderline(String url)
        {
            super(url);
        }

        @Override public void updateDrawState(TextPaint ds)
        {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
