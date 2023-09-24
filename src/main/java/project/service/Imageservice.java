package project.service;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import project.model.Imageinfo;
import project.model.User;
import project.repository.ImageRepository;
import project.repository.UserRepository;

@Service
public class Imageservice {
	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private UserRepository userRepository;

	private final Path root = Paths.get("./uploads");

	public List<Imageinfo> listAll(String keyword, String userName) {

		User user = userRepository.findByEmail(userName);
		if (keyword != null) {
			return imageRepository.findByNameAndUser(keyword, user);
		}

		return imageRepository.findByUser(user);
	}

	public void save(Imageinfo product, String email, MultipartFile file) {
		/*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasUserRole = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
		System.out.println("Has user role = " + hasUserRole);*/
		User user = userRepository.findByEmail(email);
		product.setFilename(file.getOriginalFilename());
		product.setUrl_image(this.root.resolve(file.getOriginalFilename()).toString());
		product.setUser(user);

		try {
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("A file of that name already exists.");
			}

			throw new RuntimeException(e.getMessage());
		}
		
		user.getImageinfoes().add(product);
		this.userRepository.save(user);

		// same as top but no user id
		// repo.save(product);
	}	
	
	public void save_edit(Imageinfo product, Long id) {
		Imageinfo myProduct = imageRepository.getReferenceById(id);
		Date time = new Date();
        myProduct.setName(product.getName());
        myProduct.setLastname(product.getLastname());
        myProduct.setThainationalIDcard(product.getThainationalIDcard());
        myProduct.setBuyin(product.getBuyin());
		myProduct.setTime(time);
		imageRepository.save(myProduct);

	}

	public Imageinfo get(Long id) {
		return imageRepository.findById(id).get();
	}

	public void delete(Long id) {
		imageRepository.deleteById(id);
	}
}
