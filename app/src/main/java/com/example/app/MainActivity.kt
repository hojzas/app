package com.example.app

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.app.ui.main.SectionsPagerAdapter
import com.example.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var btnAdd: Button
    private lateinit var rv: RecyclerView
    private lateinit var sqlHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Tap", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        initView()
        sqlHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener {
            addNewTestData()
            val list = sqlHelper.getAllTestData()
            Log.e("dsf", "${list.size}")
        }
    }

    private fun initView() {
        btnAdd = binding.btnAddNew
        rv = binding.rvTest
    }

    private fun addNewTestData() {
        val name = "test data"
        val wage = 140F

        val testData = Job(-1, name, wage)
        val status = sqlHelper.insertTestData(testData)
        if (status > -1) {
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}