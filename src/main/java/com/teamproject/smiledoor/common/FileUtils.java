package com.teamproject.smiledoor.common;

import com.teamproject.smiledoor.dto.BoardFileDto;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class FileUtils {

    public List<BoardFileDto> parseFileInfo(int boardNum, MultipartHttpServletRequest multiUploadFiles) throws Exception{

        if(ObjectUtils.isEmpty(multiUploadFiles)){
            return null;
        }

        List<BoardFileDto> fileList = new ArrayList<>();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");

        ZonedDateTime current = ZonedDateTime.now();

        String path = "images/" + current.format(format);

        File file = new File(path);

        if(file.exists() == false){
            file.mkdirs();
        }

        Iterator<String> iterator = multiUploadFiles.getFileNames();

        String newFileName;
        String originalFileExtension;
        String contentType;

        while (iterator.hasNext()){
            List<MultipartFile> list = multiUploadFiles.getFiles(iterator.next());

            for(MultipartFile multipartFile : list){
                if (multipartFile.isEmpty() == false){
                    contentType = multipartFile.getContentType();

                    if(ObjectUtils.isEmpty(contentType)){
                        break;
                    }

                    else{
                        if(contentType.contains("image/jpeg")){
                            originalFileExtension = ".jpg";
                        }
                        else if(contentType.contains("image/png")){
                            originalFileExtension = ".png";
                        }
                        else if(contentType.contains("image/gif")){
                            originalFileExtension = ".gif";
                        }
                        else {
                            break;
                        }
                    }

                    newFileName = Long.toString(System.nanoTime()) + originalFileExtension;

                    BoardFileDto boardFile = new BoardFileDto();
                    boardFile.setBoardNum(boardNum);
                    boardFile.setFileSize(multipartFile.getSize());
                    boardFile.setFilename(multipartFile.getOriginalFilename());
                    boardFile.setUploadpath(path + "/" + newFileName);

                    fileList.add(boardFile);

                    file = new File(path + "/" + newFileName);

                    multipartFile.transferTo(file);

                }
            }
        }


        return fileList;
    }




}
