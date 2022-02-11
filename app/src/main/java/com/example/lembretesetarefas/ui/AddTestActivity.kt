package com.example.lembretesetarefas.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lembretesetarefas.TaskDataSource
import com.example.lembretesetarefas.databinding.ActivityAddTestBinding
import com.example.lembretesetarefas.extensions.format
import com.example.lembretesetarefas.extensions.text
import com.example.lembretesetarefas.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findbyId(taskId)?.let {
                binding.tilTTTLE.text = it.title
                binding.date.text = it.date
                binding.tilHour.text = it.hour
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.date.editText?.setOnClickListener {
           val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val ofSet = timeZone.getOffset(Date().time) * -1
                binding.date.text = Date(it + ofSet ).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")

        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {

                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "${hour}:${minute}"
            }

            timePicker.show(supportFragmentManager, null)

        }
        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnNewTask.setOnClickListener {
            val task = Task(
                title = binding.tilTTTLE.text,
                date = binding.date.text,
                hour = binding.tilHour.text,
                id = intent.getIntExtra(TASK_ID, 0)
             )
            TaskDataSource.insertTask(task)

            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    companion object {
        const val TASK_ID = "task_id"
    }

}