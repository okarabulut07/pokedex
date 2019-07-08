package com.example.pokedex;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Bitmap> pokemonImage;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_pokemon, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_pokemon){

            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
            intent.putExtra("info", "new");
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        final ArrayList<String> pokemonName = new ArrayList<String>();
        pokemonImage = new ArrayList<Bitmap>();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,pokemonName);
        listView.setAdapter(arrayAdapter);

        try {

            Main2Activity.database = openOrCreateDatabase("Pokemons", MODE_PRIVATE,null);
            Main2Activity.database.execSQL("CREATE TABLE IF NOT EXISTS pokemons (name VARCHAR, image BLOB)");

            Cursor cursor = Main2Activity.database.rawQuery("SELECT * FROM pokemons", null);

            int nameIdx = cursor.getColumnIndex("name");
            int imageIdx = cursor.getColumnIndex("image");

            cursor.moveToFirst();

            while (cursor != null){
               pokemonName.add(cursor.getString(nameIdx));

               byte [] byteArray = cursor.getBlob(imageIdx);
               Bitmap image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
               pokemonImage.add(image);

               cursor.moveToNext();
               arrayAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
               intent.putExtra("info", "old");
               intent.putExtra("name", pokemonName.get(position));
               intent.putExtra("position", position);

               startActivity(intent);
           }
       });


    }
}
