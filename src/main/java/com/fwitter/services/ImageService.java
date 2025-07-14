package com.fwitter.services;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.Optional;
//import java.util.UUID;
//
//import com.fwitter.exceptions.UnableToResolvePhotoException;
//import com.fwitter.exceptions.UnableToSavePhotoException;
//import com.fwitter.models.Image;
//import com.fwitter.repositories.ImageRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//@Transactional
//public class ImageService {
//
//	private final ImageRepository imageRepository;
//
////	private static final File DIRECTORY = new File("C:\\Users\\Yahoshuva\\Downloads\\version\\version\\img");
////	C:\Users\Yahoshuva\Music\version
//	private static final File DIRECTORY = new File("C:\\Users\\Yahoshuva\\Music\\version\\img");
//
//
//
//	private static final String URL = "http://localhost:9080/images/";
//
//	@Autowired
//	public ImageService(ImageRepository imageRepository) {
//		this.imageRepository = imageRepository;
//	}
//
//	public Optional<Image> getImageByImageName(String name) {
//		return imageRepository.findByImageName(name);
//	}
//	public Image saveGifFromPost(Image image) {
//		return imageRepository.save(image);
//	}
//
//	public Image uploadImage(MultipartFile file, String prefix) throws UnableToSavePhotoException{
//		try {
//			//The content type from the request looks something like this img/jpeg
//			String extension = "." + file.getContentType().split("/")[1];
//
//			File img = File.createTempFile(prefix, extension, DIRECTORY);
//
//			file.transferTo(img);
//
//			String imageURL = URL + img.getName();
//
//			Image i = new Image(img.getName(), file.getContentType(), img.getPath(), imageURL);
//
//			Image saved = imageRepository.save(i);
//
//			return saved;
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new UnableToSavePhotoException();
//		}
//	}
//
//	public Image createOrganization(MultipartFile file, String organizationName) throws UnableToSavePhotoException {
//		try{
//			String extension = "." + file.getContentType().split("/")[1];
//			File orgImg = new File(DIRECTORY + "/" + organizationName + extension);
//			orgImg.createNewFile();
//			file.transferTo(orgImg);
//
//			String imageURL = URL + orgImg.getName();
//
//			Image i = new Image(orgImg.getName(), file.getContentType(), orgImg.getPath(), imageURL);
//
//			return imageRepository.save(i);
//
//		} catch(IOException e){
//			e.printStackTrace();
//			throw new UnableToSavePhotoException();
//		}
//	}
//
//	public byte[] downloadImage(String filename) throws UnableToResolvePhotoException{
//		try {
//			Image image = imageRepository.findByImageName(filename).get();
//
//			String filePath = image.getImagePath();
//
//			byte[] imageBytes = Files.readAllBytes(new File(filePath).toPath());
//
//			return imageBytes;
//		} catch(IOException e) {
//			throw new UnableToResolvePhotoException();
//		}
//	}
//
//	public String getImageType(String fileName) {
//		Image image = imageRepository.findByImageName(fileName).get();
//
//		return image.getImageType();
//	}
//
//    public String saveGifFromMessage(String url){
//        UUID uuid = UUID.randomUUID();
//        String gifName = "msg-" + uuid.toString();
//        Image image = new Image(gifName, "gif", url, url);
//        imageRepository.save(image);
//        return url;
//    }
//
//}


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

import com.fwitter.exceptions.UnableToResolvePhotoException;
import com.fwitter.exceptions.UnableToSavePhotoException;
import com.fwitter.models.Image;
import com.fwitter.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;


@Service
@Transactional
public class ImageService {

	private final ImageRepository imageRepository;
	private static final File DIRECTORY = new File("C:\\Users\\Yahoshuva\\Videos\\New folder (7)\\version");
	private static final String URL = "http://localhost:8000/images/";

	@Autowired
	public ImageService(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	// Existing methods for profile pictures
	public Optional<Image> getImageByImageName(String name) {
		return imageRepository.findByImageName(name);
	}

	public Image saveGifFromPost(Image image) {
		return imageRepository.save(image);
	}

	public Image uploadImage(MultipartFile file, String prefix) throws UnableToSavePhotoException {
		try {
			String extension = "." + file.getContentType().split("/")[1];
			File img = File.createTempFile(prefix, extension, DIRECTORY);
			file.transferTo(img);

			String imageURL = URL + img.getName();
			Image i = new Image(img.getName(), file.getContentType(), img.getPath(), imageURL);
			return imageRepository.save(i);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UnableToSavePhotoException();
		}
	}

	public Image updateImageFromBase64(String data, String prefix) throws UnableToSavePhotoException {
		try{
			String extension = "." + data.split("image/")[1].split(";")[0];
			String base64Image = data.split(",")[1];

			byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);

			BufferedImage parsedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

			File img = File.createTempFile(prefix, extension, DIRECTORY);

			ImageIO.write(parsedImage, "jpeg", img);

			String imageURL = URL + img.getName();

			Image i = new Image(img.getName(),  "image/"+extension, img.getPath(), imageURL);

			Image saved = imageRepository.save(i);

			return saved;
		} catch(IOException e){
			throw new UnableToSavePhotoException();
		}
	}

	public Image createOrganization(MultipartFile file, String organizationName) throws UnableToSavePhotoException {
		try {
			String extension = "." + file.getContentType().split("/")[1];
			File orgImg = new File(DIRECTORY + "\\" + organizationName + extension);
			orgImg.createNewFile();
			file.transferTo(orgImg);

			String imageURL = URL + orgImg.getName();
			Image i = new Image(orgImg.getName(), file.getContentType(), orgImg.getPath(), imageURL);
			return imageRepository.save(i);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UnableToSavePhotoException();
		}
	}

	public byte[] downloadImage(String filename) throws UnableToResolvePhotoException {
		try {
			Image image = imageRepository.findByImageName(filename).get();
			String filePath = image.getImagePath();
			return Files.readAllBytes(new File(filePath).toPath());
		} catch (IOException e) {
			throw new UnableToResolvePhotoException();
		}
	}

	public String getImageType(String fileName) {
		Image image = imageRepository.findByImageName(fileName).get();
		return image.getImageType();
	}

	public String saveGifFromMessage(String url) {
		UUID uuid = UUID.randomUUID();
		String gifName = "msg-" + uuid.toString();
		Image image = new Image(gifName, "gif", url, url);
		imageRepository.save(image);
		return url;
	}

	// New method for handling post images
	public Image uploadPostImage(MultipartFile file) throws UnableToSavePhotoException {
		try {
			// Generate a unique prefix for the post image
			String prefix = "post-" + UUID.randomUUID().toString();
			String extension = "." + file.getContentType().split("/")[1];

			// Create the file in the directory
			File img = File.createTempFile(prefix, extension, DIRECTORY);
			file.transferTo(img);

			// Generate the image URL
			String imageURL = URL + img.getName();

			// Create and save the Image entity
			Image image = new Image(img.getName(), file.getContentType(), img.getPath(), imageURL);
			return imageRepository.save(image);
		} catch (IOException e) {
			e.printStackTrace();
			throw new UnableToSavePhotoException();
		}
	}
}