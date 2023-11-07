package project.service;

import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

//import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import project.controller.FileController;
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
		/*
		 * Authentication authentication =
		 * SecurityContextHolder.getContext().getAuthentication();
		 * boolean hasUserRole = authentication.getAuthorities().stream()
		 * .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
		 * System.out.println("Has user role = " + hasUserRole);
		 */
		String nameFile = System.nanoTime() + file.getOriginalFilename(); 

		try {
			Files.copy(file.getInputStream(), this.root.resolve(nameFile));
		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("A file of that name already exists.");
			}

			throw new RuntimeException(e.getMessage());
		}

		try {
			Path pate_file = root.resolve(nameFile);
			Resource resource = new UrlResource(pate_file.toUri());
			System.out.println("URL image " + resource);
			String url = MvcUriComponentsBuilder
					.fromMethodName(FileController.class, "getFile", pate_file.getFileName().toString()).build()
					.toString();
			product.setUrl_image(url);
			
			/*
			 * if (!(resource.exists() || resource.isReadable())) {
			 * String url = MvcUriComponentsBuilder
			 * .fromMethodName(FileController.class, "getFile",
			 * pate_file.getFileName().toString()).build()
			 * .toString();
			 * product.setUrl_image(url);
			 * } else {
			 * throw new RuntimeException("File is exist");
			 * }
			 */
			
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
		User user = userRepository.findByEmail(email);
		product.setFilename(nameFile);
		product.setUser(user);

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
