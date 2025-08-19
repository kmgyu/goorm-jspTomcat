package main.java.io.goorm.backend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import main.java.io.goorm.backend.config.DatabaseConfig;

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
    board.setCreatedAt(rs.getTimestamp("createdAt"));
    return board;
  };

  public List<Board> getBoardList() {
    String sql = "SELECT * FROM board ORDER BY id DESC";
    return jdbcTemplate.query(sql, boardRowMapper);
  }

  public Board getBoardById(int id) {
    String sql = "SELECT * FROM board WHERE id = ?";
    try {
      return jdbcTemplate.queryForObject(sql, boardRowMapper, id);
    } catch (Exception e) {
      return null;
    }
  }

  public void insertBoard(Board board) {
    String sql = "INSERT INTO board (title, writer, content, reg_date) VALUES (?, ?, ?, NOW())";
    jdbcTemplate.update(sql, board.getTitle(), board.getAuthor(), board.getContent());
  }

  // BoardDAO 클래스에 추가할 메서드
  public List<Board> searchByTitle(String keyword) {
    String sql = "SELECT * FROM board WHERE title LIKE ? ORDER BY id DESC";
    String searchKeyword = "%" + keyword + "%";
    return jdbcTemplate.query(sql, boardRowMapper, searchKeyword);
  }

  // 게시글 수정 메서드도 추가
  public void updateBoard(Board board) {
    String sql = "UPDATE board SET title = ?, content = ? WHERE id = ?";
    jdbcTemplate.update(sql, board.getTitle(), board.getContent(), board.getId());
  }

  // 게시글 삭제 메서드도 추가
  public boolean deleteBoard(int id) {
    String sql = "DELETE FROM board WHERE id = ?";
    int result = jdbcTemplate.update(sql, id);
    return result > 0;
  }
}