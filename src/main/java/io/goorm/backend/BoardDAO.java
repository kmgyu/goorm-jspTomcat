package io.goorm.backend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import io.goorm.backend.config.DatabaseConfig;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {

  private JdbcTemplate jdbcTemplate;

  public BoardDAO() {
    this.jdbcTemplate = new JdbcTemplate(DatabaseConfig.getDataSource());
  }

  // RowMapper 정의
  private RowMapper<Board> boardRowMapper = (rs, rowNum) -> {
    Board board = new Board();
    board.setId(rs.getLong("id"));
    board.setTitle(rs.getString("title"));
    board.setAuthor(rs.getString("author"));
    board.setContent(rs.getString("content"));
    board.setWriterId(rs.getInt("writer_id"));
    board.setCreatedAt(rs.getTimestamp("created_at"));
    return board;
  };

  public List<Board> getBoardList() {
    String sql = "SELECT b.*, u.name as author_name FROM board b " +
            "LEFT JOIN users u ON b.author = u.id " +
            "ORDER BY b.created_at DESC";
    return jdbcTemplate.query(sql, boardRowMapper);
  }

  public Board getBoardById(long id) {
    String sql = "SELECT b.*, u.name as author_name FROM board b " +
            "LEFT JOIN users u ON b.author = u.id " +
            "WHERE b.id = ?";
    try {
      Board board = jdbcTemplate.queryForObject(sql, boardRowMapper, id);
      if (board != null) {
        // 첨부파일 정보도 함께 조회
        loadAttachments(board);
      }
      return board;
    } catch (Exception e) {
      return null;
    }
  }

  public Long insertBoard(Board board) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    String sql = "INSERT INTO board (title, content, author) VALUES (?, ?, ?)";

    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, new String[] { "id" });
      ps.setString(1, board.getTitle());
      ps.setString(2, board.getContent());
      ps.setString(3, board.getAuthor());
      return ps;
    }, keyHolder);

    Long generatedId = keyHolder.getKey().longValue();
    board.setId(generatedId);
    System.out.println("생성된 게시글 ID: " + board.getId());

    return generatedId; // 생성된 board_id 반환
  }

  public boolean updateBoard(Board board) {
    String sql = "UPDATE board SET title = ?, content = ? WHERE id = ?";
    int result = jdbcTemplate.update(sql, board.getTitle(), board.getContent(), board.getId());
    return result > 0;
  }

  /**
   * 게시글 삭제
   */
  public boolean deleteBoard(Long id) {
    String sql = "DELETE FROM board WHERE id = ?";
    int result = jdbcTemplate.update(sql, id);
    return result > 0;
  }

  /**
   * 제목으로 게시글 검색
   */
  public List<Board> searchByTitle(String keyword) {
    String sql = "SELECT * FROM board WHERE title LIKE ? ORDER BY created_at DESC";
    String searchKeyword = "%" + keyword + "%";
    return jdbcTemplate.query(sql, boardRowMapper, searchKeyword);
  }

  /**
   * 게시글의 첨부파일 정보 로드
   */
  private void loadAttachments(Board board) {
    try {
      FileUploadDAO fileUploadDAO = new FileUploadDAO();
      List<FileUpload> attachments = fileUploadDAO.getFilesByBoardId(board.getId());
      board.setAttachments(attachments);
    } catch (Exception e) {
      e.printStackTrace();
      board.setAttachments(new ArrayList<>());
    }
  }
}