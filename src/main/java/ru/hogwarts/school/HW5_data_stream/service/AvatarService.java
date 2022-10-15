package ru.hogwarts.school.HW5_data_stream.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.HW5_data_stream.model.Avatar;
import ru.hogwarts.school.HW5_data_stream.model.Student;
import ru.hogwarts.school.HW5_data_stream.repository.AvatarRepository;
import ru.hogwarts.school.HW5_data_stream.repository.StudentRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    //private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {// сохранение инфо на сервере
            Student student = studentRepository.findById(studentId).orElse(null);
            Path filePath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));//это
        // путь к файлу имя будет присваиваться по student, через studentId, с расширением файла, которое было задано(оригинальное имя
        // которое пользователь в браузере задал)
        //название этого файла новое каждый раз
            Files.createDirectories(filePath.getParent()); // проверка, всели папки по этому пути созданы и создание в случае отсуствия
            Files.deleteIfExists(filePath);//чтобы можно было сделать апдейт, если картинка уже есть по этому id
            try ( // здесь потоки для того чтобы загрузить на сервис, которые закрываются здесь же
                    InputStream is = avatarFile.getInputStream();// входной поток на считывание файла, загруженного в браузере пользователем
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);//создаем место куда будем добавлять данные
                    BufferedInputStream bis = new BufferedInputStream(is, 1024); // используем буферизированные потоки данных
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            ) {
                bis.transferTo(bos);// передача данных из вх в вых поток
            }
            Avatar avatar = findAvatar(studentId); //находим аватар по id студента, если нет то создается новый
            avatar.setStudent(student);//указываем студента к кому этот аватар
            avatar.setFilePath(filePath.toString());//указываем путь к файлу, который мы сохранили на диске
            avatar.setFileSize(avatarFile.getSize());//указываем размер
            avatar.setMediaType(avatarFile.getContentType());// указываем тип контента
            avatar.setData(avatarFile.getBytes());// сохранение в базе данных
            avatarRepository.save(avatar);//
        }

    public Avatar findAvatar(long studentId) {
        return avatarRepository.findAvatarByStudentId(studentId).orElse(new Avatar());
    }

    private String getExtensions(String fileName) { //метод находит последнюю точку
        // в нашей строке и возвращает все, что находится после этой точки
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
}
