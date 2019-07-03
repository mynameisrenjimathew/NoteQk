package com.renzam.noteqk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    RecyclerView recyclerView;
    public static final int AddNoteReqst = 1;
    public static final int EditNoteRequst = 2;

    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), AddEditNoteActivity.class),AddNoteReqst);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update Recycler
                noteAdapter.setNotes(notes);
            }
        });
        //recycler on Swipable
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnitemClickListener() {
            @Override
            public void onitemClick(Note note) {
                Intent intent = new Intent(getApplicationContext(), AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXSTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXSTRA_TITLE,note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXSTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXSTRA_PRIORITY,note.getPriority());
                startActivityForResult(intent,EditNoteRequst);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddNoteReqst && resultCode == RESULT_OK){

            String title = data.getStringExtra(AddEditNoteActivity.EXSTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXSTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXSTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note Saved :)", Toast.LENGTH_SHORT).show();

        }else if (requestCode == EditNoteRequst && resultCode == RESULT_OK){

            int id = data.getIntExtra(AddEditNoteActivity.EXSTRA_ID,-1);
            if (id == -1){
                Toast.makeText(this, "Cant edit", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNoteActivity.EXSTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXSTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXSTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();



        } else{
            Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.deleteAllNotesId){

            noteViewModel.deleteAllNotes();
            return true;

        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
