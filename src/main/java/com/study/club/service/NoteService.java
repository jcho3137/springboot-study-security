package com.study.club.service;

import com.study.club.dto.NoteDTO;
import com.study.club.entity.ClubMember;
import com.study.club.entity.Note;
import org.aspectj.weaver.ast.Not;

import java.util.List;

public interface NoteService {
    Long register(NoteDTO noteDTO);
    NoteDTO get(Long num);
    void modify(NoteDTO noteDTO);
    void remove(Long num);
    List<NoteDTO> getAllWithWriter(String writerEmail);
    default Note dtoToEntity(NoteDTO noteDTO) {
        return Note.builder()
                .num(noteDTO.getNum())
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .writer(ClubMember.builder().email(noteDTO.getWriterEmail()).build())
                .build();
    }

    default NoteDTO entityToDTo(Note note) {
        return NoteDTO.builder()
                .num(note.getNum())
                .title(note.getTitle())
                .content(note.getContent())
                .writerEmail(note.getWriter().getEmail())
                .regDate(note.getRegDate())
                .modDate(note.getModDate())
                .build();
    }
}
