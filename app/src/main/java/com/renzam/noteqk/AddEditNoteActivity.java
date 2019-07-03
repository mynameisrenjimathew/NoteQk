package com.renzam.noteqk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {

    private EditText editTextTitle,editTextDescription;
    private NumberPicker numberPickerPriority;

    public static final String EXSTRA_ID = "com.renzam.noteqk.EXSTRA_ID";
    public static final String EXSTRA_TITLE = "com.renzam.noteqk.EXSTRA_TITLE";
    public static final String EXSTRA_DESCRIPTION = "com.renzam.noteqk.EXSTRA_DESCRIPTION";
    public static final String EXSTRA_PRIORITY = "com.renzam.noteqk.EXSTRA_PRIORITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority =findViewById(R.id.numberpicker);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);



        Intent intent = getIntent();
        if(intent.hasExtra(EXSTRA_ID)){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXSTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXSTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXSTRA_PRIORITY,1));
        }else{
            setTitle("Add Note");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.saveNotes){

            saveNote();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }


    }

    private void saveNote() {

        String title =  editTextTitle.getText().toString();
        String description =editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.isEmpty()){

            Toast.makeText(this, "Sorry Title or Description is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXSTRA_TITLE,title);
        data.putExtra(EXSTRA_DESCRIPTION,description);
        data.putExtra(EXSTRA_PRIORITY,priority);

        int id = getIntent().getIntExtra(EXSTRA_ID,-1);

        if (id != -1){
            data.putExtra(EXSTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();


        }
    }
