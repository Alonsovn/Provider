package com.provider.app.util;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Row;

@Component
public class FileUtil {
	
	
	public Supplier<Stream<Row>> getRowStreamSupplier(Iterable<Row> rows) {
		return () -> getStream(rows);
	}

	public <T> Stream<T> getStream(Iterable<T> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false);
	}
	
	public Supplier<Stream<Integer>> cellIteratorSupplier(int end) {
		return () -> numberStream(end);
	}

	public Stream<Integer> numberStream(int end) {
		return IntStream.range(0, end).boxed();
	}
}
