package maustemies.passprotecter;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class OpenFileActivity extends AppCompatActivity {

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_file);

        listView = (ListView) findViewById(R.id.listView);

        final ArrayList<String> fileNames = new ArrayList<>();
        File dirRoot = getApplicationContext().getFilesDir();
        for(File file : dirRoot.listFiles()) {
            if(file.isFile()) {
                Log.d(Constants.OPEN_FILE_ACTIVITY_TAG, "Found file with name: " + file.getName());
                fileNames.add(file.getName());
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(Constants.FILENAME_TAG, fileNames.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
