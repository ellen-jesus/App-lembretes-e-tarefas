package com.example.lembretesetarefas

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lembretesetarefas.databinding.ActivityMainBinding
import com.example.lembretesetarefas.model.Task
import com.example.lembretesetarefas.ui.AddTestActivity
import com.example.lembretesetarefas.ui.AddTestActivity.Companion.TASK_ID
import com.example.lembretesetarefas.ui.TaskListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTask.adapter = adapter
        updateList()

          insertlisterners()
    }

    private fun insertlisterners() {
        binding.fab.setOnClickListener {
            startActivityForResult(Intent(this, AddTestActivity::class.java), CREAT_NEW_TASK) //chama a outra activity
        }

        adapter.listenerEdit =  {
           val intent= Intent(this, AddTestActivity::class.java)
            intent.putExtra(AddTestActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREAT_NEW_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREAT_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
    }

    private fun updateList() {
        val list = TaskDataSource.getList()
        if(list.isEmpty()){
            binding.includeEmpty.emptyState.visibility = View.VISIBLE
        }else{
            binding.includeEmpty.emptyState.visibility = View.GONE
        }
        adapter.submitList(list)
    }

    companion object {
        private const val CREAT_NEW_TASK = 1000
    }
}