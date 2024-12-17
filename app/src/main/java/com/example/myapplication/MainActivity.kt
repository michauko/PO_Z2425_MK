package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


// klasa reprezentujaca zadanie, implementuje serializacje, co pozwala na zapis i odczyt obiektu w pliku
data class Task(
    val day: String,      // dzień tygodnia
    val time: String,     // godzina
    val description: String // opis zadania
) : java.io.Serializable

class MainActivity : AppCompatActivity() {

    // lista zadań przechowująca wszystkie obiekty typu Task
    private val tasks = mutableListOf<Task>()

    // referencje do widoków RecyclerView i Adaptera
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // inicjalizacja RecyclerView i adaptera
        recyclerView = findViewById(R.id.recyclerView)
        taskAdapter = TaskAdapter(tasks, this)
        recyclerView.layoutManager = LinearLayoutManager(this) // ustawienie RecyclerView jako listy pionowej
        recyclerView.adapter = taskAdapter // podłączenie adaptera do RecyclerView

        // obsługa przycisku dodawania zadania
        findViewById<View>(R.id.addTaskButton).setOnClickListener {
            showAddTaskDialog() // wyświetlenie dodania zadania
        }

        loadTasks() // wczytanie zapisanych zadań z pliku
    }

    // funkcja wyświetlająca dodawanie lub edycje zadania
    private fun showAddTaskDialog(taskToEdit: Task? = null, position: Int? = null) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)

        // referencje do elementów UI
        val daySpinner: Spinner = dialogView.findViewById(R.id.daySpinner)
        val hourSpinner: Spinner = dialogView.findViewById(R.id.hourSpinner)
        val minuteSpinner: Spinner = dialogView.findViewById(R.id.minuteSpinner)
        val descriptionInput: TextView = dialogView.findViewById(R.id.descriptionInput)

        // inicjalizacja spinnera wyboru dnia tygodnia
        val days = listOf("Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota", "Niedziela")
        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daySpinner.adapter = dayAdapter

        // inicjalizacja spinnera wyboru godziny
        val hours = (0..23).map { String.format("%02d", it) }
        val hourAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hours)
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        hourSpinner.adapter = hourAdapter

        // inicjalizacja spinnera wyboru minut
        val minutes = (0..55 step 5).map { String.format("%02d", it) }
        val minuteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, minutes)
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        minuteSpinner.adapter = minuteAdapter

        // wypełnianie pól istniejącym zadaniem podczas edycji
        if (taskToEdit != null) {
            daySpinner.setSelection(days.indexOf(taskToEdit.day))
            val (hour, minute) = taskToEdit.time.split(":")
            hourSpinner.setSelection(hours.indexOf(hour))
            minuteSpinner.setSelection(minutes.indexOf(minute))
            descriptionInput.text = taskToEdit.description
        }

        // wyświetlenie opcji dodania lub edycji zadania
        AlertDialog.Builder(this)
            .setTitle(if (taskToEdit == null) "Dodaj zadanie" else "Edytuj zadanie")
            .setView(dialogView)
            .setPositiveButton(if (taskToEdit == null) "Dodaj" else "Zapisz") { _, _ ->
                val selectedDay = daySpinner.selectedItem.toString()
                val selectedHour = hourSpinner.selectedItem.toString()
                val selectedMinute = minuteSpinner.selectedItem.toString()
                val description = descriptionInput.text.toString()
                val time = "$selectedHour:$selectedMinute"

                // dodanie nowego zadania lub aktualizacja istniejącego
                if (taskToEdit == null) {
                    tasks.add(Task(day = selectedDay, time = time, description = description))
                } else if (position != null) {
                    tasks[position] = Task(day = selectedDay, time = time, description = description)
                }

                taskAdapter.notifyDataSetChanged() // odświeżenie widoku RecyclerView
                saveTasks() // zapisanie zadań do pliku
            }
            .setNegativeButton("Anuluj", null) // anulowanie dialogu
            .show()
    }

    // funkcja zapisująca listę zadań do pliku
    private fun saveTasks() {
            val outputStream = openFileOutput("tasks.dat", Context.MODE_PRIVATE) // tworzenie prywatnego pliku
            val objectOutputStream = ObjectOutputStream(outputStream)
            objectOutputStream.writeObject(tasks) // serializacja listy zadań
            objectOutputStream.close()
            outputStream.close()
    }

    // funkcja wczytująca zapisane zadania z pliku
    private fun loadTasks() {
            val inputStream = openFileInput("tasks.dat") // otwieranie pliku z zapisanymi zadaniami
            val objectInputStream = ObjectInputStream(inputStream)
            val loadedTasks = objectInputStream.readObject() as? MutableList<Task> // deserializacja listy zadań
            if (loadedTasks != null) {
                tasks.clear()
                tasks.addAll(loadedTasks) // załadowanie zadań do listy
                taskAdapter.notifyDataSetChanged() // odświeżenie RecyclerView
            }
            objectInputStream.close()
            inputStream.close()
    }

    // klasa adaptera dla RecyclerView, zarządza wyświetlaniem i obsługą listy zadań
    inner class TaskAdapter(
        private val tasks: MutableList<Task>, // lista zadań do wyświetlenia
        private val context: MainActivity
    ) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

        // klasa widoku pojedynczego zadania
        inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val dayText: TextView = view.findViewById(R.id.taskDay)
            val timeText: TextView = view.findViewById(R.id.taskTime)
            val descriptionText: TextView = view.findViewById(R.id.taskDescription)
            val editButton: ImageButton = view.findViewById(R.id.editButton)
            val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
        }

        // tworzenie nowego widoku dla elementu RecyclerView
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
            return TaskViewHolder(view)
        }

        // wiązanie danych z listy z widokiem
        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            val task = tasks[position]
            holder.dayText.text = task.day
            holder.timeText.text = task.time
            holder.descriptionText.text = task.description

            // obsługa przycisku edycji zadania
            holder.editButton.setOnClickListener {
                context.showAddTaskDialog(taskToEdit = task, position = position) // wyświetlenie dialogu edycji
            }

            // obsługa przycisku usuwania zadania
            holder.deleteButton.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Usuń zadanie")
                    .setMessage("Na pewno chcesz usunąć?")
                    .setPositiveButton("Tak") { _, _ ->
                        tasks.removeAt(position) // usunięcie zadania z listy
                        notifyDataSetChanged() // odświeżenie RecyclerView
                        context.saveTasks() // zapisanie zmian w pliku
                    }
                    .setNegativeButton("Nie", null) // anulowanie usunięcia
                    .show()
            }
        }

        // liczba elementów w liście zadań (niezbędne dla RecyclerView)
        override fun getItemCount(): Int = tasks.size
    }
}
