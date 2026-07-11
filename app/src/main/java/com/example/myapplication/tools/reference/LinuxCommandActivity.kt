package com.example.myapplication.tools.reference

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import io.noties.markwon.Markwon
import java.io.File

class LinuxCommandActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var lvCommands: ListView
    private val commands = mutableListOf<CommandItem>()
    private val filteredCommands = mutableListOf<CommandItem>()
    private lateinit var markwon: Markwon

    data class CommandItem(val name: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linux_command)

        markwon = Markwon.create(this)

        etSearch = findViewById(R.id.et_search)
        lvCommands = findViewById(R.id.lv_commands)

        loadCommands()

        lvCommands.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredCommands.map { it.name })
        lvCommands.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            showCommandDetail(filteredCommands[position])
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { filter(s.toString()) }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadCommands() {
        try {
            val files = assets.list("linux-command") ?: return
            for (fileName in files) {
                if (fileName.endsWith(".md")) {
                    commands.add(CommandItem(fileName.removeSuffix(".md")))
                }
            }
            commands.sortBy { it.name }
            filteredCommands.addAll(commands)
        } catch (e: Exception) {
            Toast.makeText(this, "加载命令失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filter(query: String) {
        filteredCommands.clear()
        filteredCommands.addAll(if (query.isBlank()) commands else commands.filter { it.name.contains(query, true) })
        lvCommands.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredCommands.map { it.name })
    }

    private fun showCommandDetail(cmd: CommandItem) {
        val content = try {
            assets.open("linux-command/${cmd.name}.md").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            "无法加载命令详情"
        }

        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle(cmd.name)
            .setPositiveButton("关闭", null)
            .create()

        val scrollView = ScrollView(this)
        val tv = TextView(this).apply { setPadding(16, 16, 16, 16) }
        markwon.setMarkdown(tv, content)
        scrollView.addView(tv)
        dialog.setView(scrollView)
        dialog.show()
    }
}
