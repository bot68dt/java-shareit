package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapperNew {
    public static CommentResponseDto mapToCommentResponseDto(Comment comment) {
        return new CommentResponseDto(comment.getId(), comment.getText(), comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static List<CommentResponseDto> mapToCommentResponseDto(Iterable<Comment> comments) {
        List<CommentResponseDto> result = new ArrayList<>();

        for (Comment comment : comments) {
            result.add(mapToCommentResponseDto(comment));
        }

        return result;
    }
}
