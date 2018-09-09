package youcompany.find.findyoucompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void lanzarMapa(View vista){
        Intent inte= new Intent(this, mapas.class);
        startActivity(inte);
    }
}
