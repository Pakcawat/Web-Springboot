package project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.model.Imageinfo;
import project.model.User;

public interface ImageRepository extends JpaRepository<Imageinfo, Long> {
	
	Imageinfo findById(long id);
    Imageinfo findByName(String name);

    List<Imageinfo> findAllByOrderByIdAsc();
	List<Imageinfo> findByUser(User user);

	@Query("SELECT p FROM Imageinfo p WHERE p.name LIKE %?1%")
	public List<Imageinfo> search(String keyword);

	@Query("SELECT e FROM Imageinfo e WHERE e.name = :keyword AND e.user = :user")
	List<Imageinfo> findByNameAndUser(String keyword, User user);

}
