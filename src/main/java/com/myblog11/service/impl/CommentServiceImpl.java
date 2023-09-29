package com.myblog11.service.impl;

import com.myblog11.entity.Comment;
import com.myblog11.entity.Post;
import com.myblog11.exception.BlogApi;
import com.myblog11.exception.ResourceNotFound;
import com.myblog11.payload.CommentDto;
import com.myblog11.repository.CommentRepository;
import com.myblog11.repository.PostRepository;
import com.myblog11.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {


    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }


    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with this id"  +postId));
        // set post to comment entity
        comment.setPost(post);
        // comment entity to DB
        Comment newComment = commentRepository.save(comment);
        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);
        // convert list of comment entities to list of comment dto's
        return comments.stream().map(comment ->
                mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("not available" +postId));
        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->
                new ResourceNotFound("not available" +commentId));
        if(!Objects.equals(comment.getPost().getId(), post.getId())){
            throw new BlogApi(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound(""  +postId));
        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFound(""  +commentId));
        if(!Objects.equals(comment.getPost().getId(), post.getId())){
            throw new BlogApi(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("post doen not found with this id" +postId));
        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFound("comment does not found with this id" +commentId));
        if(!Objects.equals(comment.getPost().getId(), post.getId())){
            throw new BlogApi(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }
        commentRepository.delete(comment);
    }




    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
       // CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }
    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = mapper.map(commentDto, Comment.class);
     //   Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
}

