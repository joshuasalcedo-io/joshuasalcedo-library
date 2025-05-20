package io.joshuasalcedo.pretty.core.model.error;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between java.lang.StackTraceElement and StackTraceElementDTO.
 * Since StackTraceElement is a final class in java.lang, we can't extend it and need
 * to use composition for mapping.
 */
public class StackTraceElementMapper {

    /**
     * Converts a StackTraceElement to a StackTraceElementDTO
     *
     * @param element the StackTraceElement to convert
     * @return the corresponding DTO
     */
    public static StackTraceElementDTO toDto(StackTraceElement element) {
        if (element == null) {
            return null;
        }
        
        return new StackTraceElementDTO(
            element.getClassLoaderName(),
            element.getModuleName(),
            element.getModuleVersion(),
            element.getClassName(),
            element.getMethodName(),
            element.getFileName(),
            element.getLineNumber()
        );
    }
    
    /**
     * Converts a StackTraceElementDTO to a StackTraceElement
     *
     * @param dto the DTO to convert
     * @return the corresponding StackTraceElement
     */
    public static StackTraceElement toEntity(StackTraceElementDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return new StackTraceElement(
            dto.getClassLoaderName(),
            dto.getModuleName(),
            dto.getModuleVersion(),
            dto.getDeclaringClass(),
            dto.getMethodName(),
            dto.getFileName(),
            dto.getLineNumber()
        );
    }
    
    /**
     * Converts an array of StackTraceElements to a List of DTOs
     *
     * @param elements the array of StackTraceElements
     * @return a list of corresponding DTOs
     */
    public static List<StackTraceElementDTO> toDtoList(StackTraceElement[] elements) {
        if (elements == null) {
            return null;
        }
        
        return Arrays.stream(elements)
                .map(StackTraceElementMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Converts a List of StackTraceElementDTOs to an array of StackTraceElements
     *
     * @param dtos the list of DTOs
     * @return an array of corresponding StackTraceElements
     */
    public static StackTraceElement[] toEntityArray(List<StackTraceElementDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(StackTraceElementMapper::toEntity)
                .toArray(StackTraceElement[]::new);
    }
}