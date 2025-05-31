package com.example.todoapp.controller;

import com.example.todoapp.model.Note;
import com.example.todoapp.model.Todo;
import com.example.todoapp.repository.NoteRepository;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Note> getNotesByUserId(@PathVariable Long userId) {
        return noteRepository.findByUser_Id(userId);
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        if (note.getUser() == null && note.getUserId() != null) {
            userRepository.findById(note.getUserId())
                .ifPresent(user -> {
                    note.setUser(user);
                    user.addNote(note);
                });
        }
        return noteRepository.save(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        return noteRepository.findById(id)
            .map(note -> {
                noteRepository.delete(note);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{noteId}/todos")
    public ResponseEntity<?> addTodoToNote(
            @PathVariable Long noteId,
            @RequestBody Todo todo) {
        return noteRepository.findById(noteId)
            .map(note -> {
                todo.setNote(note);
                Todo savedTodo = todoRepository.save(todo);
                note.addTodo(savedTodo);
                return ResponseEntity.ok(savedTodo);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{noteId}/todos/{todoId}")
    public ResponseEntity<?> updateTodoInNote(
            @PathVariable Long noteId,
            @PathVariable Long todoId,
            @RequestBody Todo todoUpdate) {
        if (!noteRepository.existsById(noteId)) {
            return ResponseEntity.notFound().build();
        }

        return todoRepository.findById(todoId)
            .map(todo -> {
                todo.setCompleted(todoUpdate.isCompleted());
                Todo updatedTodo = todoRepository.save(todo);
                return ResponseEntity.ok(updatedTodo);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{noteId}/todos/{todoId}")
    public ResponseEntity<?> deleteTodoFromNote(
            @PathVariable Long noteId,
            @PathVariable Long todoId) {
        if (!noteRepository.existsById(noteId)) {
            return ResponseEntity.notFound().build();
        }

        return todoRepository.findById(todoId)
            .map(todo -> {
                todoRepository.delete(todo);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
} 