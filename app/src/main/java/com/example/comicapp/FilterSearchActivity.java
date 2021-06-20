package com.example.comicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicapp.Adapter.MyComicAdapter;
import com.example.comicapp.Common.Common;
import com.example.comicapp.Model.Comic;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterSearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recycler_filter_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        recycler_filter_search = findViewById(R.id.recycler_filter_search);
        recycler_filter_search.setHasFixedSize(true);
        recycler_filter_search.setLayoutManager(new GridLayoutManager(this, 2));


        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_filter:
                        showFilterDisplayDialog();
                        break;
                    case R.id.action_search:
                        showSearchDialog();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search");

        final LayoutInflater inflater = this.getLayoutInflater();
        View search_layout = inflater.inflate(R.layout.dialog_search, null);

        final EditText edt_search = search_layout.findViewById(R.id.edt_search);
        builder.setView(search_layout).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                fetchSearchComic(edt_search.getText().toString());
            }
        }).show();

    }

    private void fetchSearchComic(String query) {
        List<Comic> comic_search = new ArrayList<>();
        for (Comic comic : Common.comicList) {
            if (comic.Name.contains(query)) {
                comic_search.add(comic);
            }
        }
        if (comic_search.size() > 0)
            recycler_filter_search.setAdapter(new MyComicAdapter(getBaseContext(), comic_search));
        else
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show();
    }

    private void fetchFilterCategory(String query) {
        List<Comic> comic_filtered = new ArrayList<>();
        for (Comic comic : Common.comicList) {
            if (comic.Category != null) {
                if (comic.Category.contains(query))
                    comic_filtered.add(comic);
            }
        }
        if (comic_filtered.size() > 0)
            recycler_filter_search.setAdapter(new MyComicAdapter(getBaseContext(), comic_filtered));
        else
            Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show();
    }

    private void showFilterDisplayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Category");

        final LayoutInflater inflater = this.getLayoutInflater();
        View filter_layout = inflater.inflate(R.layout.dialog_options, null);
        final AutoCompleteTextView txt_category = filter_layout.findViewById(R.id.txt_category);
        final ChipGroup chipGroup = filter_layout.findViewById(R.id.chipGroup);

        //create autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, Common.categories);
        txt_category.setAdapter(adapter);

        txt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                txt_category.setText("");

                //create tags;
                Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                chip.setText(((TextView) view).getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chipGroup.removeView(view);
                    }
                });
                chipGroup.addView(chip);
            }
        });

        builder.setView(filter_layout);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                List<String> filter_key = new ArrayList<>();
                StringBuilder filter_query = new StringBuilder("");

                for (int j = 0; j < chipGroup.getChildCount(); j++) {
                    Chip chip = (Chip) chipGroup.getChildAt(j);
                    filter_key.add(chip.getText().toString());
                }
                //because in out database ,category will be sort from a -z and split by,
                //So we need sort our filter Key
                Collections.sort(filter_key);
                //Convert list to String
                for (String key : filter_key) {
                    filter_query.append(key).append(",");
                }
                //remove last ","
                filter_query.setLength(filter_query.length() - 1);

                //filter by this query
                fetchFilterCategory(filter_query.toString());

            }
        }).show();

    }
}