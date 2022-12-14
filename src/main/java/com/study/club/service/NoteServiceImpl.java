package com.study.club.service;

import com.study.club.dto.NoteDTO;
import com.study.club.entity.Note;
import com.study.club.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public Long register(NoteDTO noteDTO) {
        log.info("note register...");
        Note note = dtoToEntity(noteDTO);
        noteRepository.save(note);
        return note.getNum();
    }

    @Override
    public NoteDTO get(Long num) {
        log.info("note get...");
        Optional<Note> result = noteRepository.getWithWriter(num);
        if(result.isPresent()) {
            return entityToDTo(result.get());
        }
        return null;
    }

    @Override
    public void modify(NoteDTO noteDTO) {
        Long num = noteDTO.getNum();
        Optional<Note> result = noteRepository.findById(num);
        if(result.isPresent()) {
            Note note = result.get();
            note.changeTitle(noteDTO.getTitle());
            note.changeContent(noteDTO.getContent());
            noteRepository.save(note);
        }
    }

    @Override
    public void remove(Long num) {
        log.info("note remove...");
        noteRepository.deleteById(num);
    }

    @Override
    public List<NoteDTO> getAllWithWriter(String writerEmail) {
        List<Note> noteList = noteRepository.getList(writerEmail);
        return noteList.stream().map(note -> entityToDTo(note)).collect(Collectors.toList());
    }
}
