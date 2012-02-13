package org.jphototagger.repository.hsqldb;

import java.util.List;

import org.openide.util.lookup.ServiceProvider;

import org.jphototagger.domain.repository.WordsetsRepository;
import org.jphototagger.domain.wordsets.Wordset;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = WordsetsRepository.class)
public final class WordsetsRepositoryImpl implements WordsetsRepository {

    @Override
    public List<Wordset> findAll() {
        return WordsetsDatabase.INSTANCE.findAll();
    }

    @Override
    public Wordset find(String wordsetName) {
        return WordsetsDatabase.INSTANCE.find(wordsetName);
    }

    @Override
    public List<String> findAllWordsetNames() {
        return WordsetsDatabase.INSTANCE.findAllWordsetNames();
    }

    @Override
    public boolean insert(Wordset wordset) {
        return WordsetsDatabase.INSTANCE.insert(wordset);
    }

    @Override
    public boolean remove(String wordsetName) {
        return WordsetsDatabase.INSTANCE.remove(wordsetName);
    }

    @Override
    public boolean existsWordset(String wordsetName) {
        return WordsetsDatabase.INSTANCE.existsWordset(wordsetName);
    }

    @Override
    public boolean addToWords(String wordsetName, String word) {
        return WordsetsDatabase.INSTANCE.addToWords(wordsetName, word);
    }

    @Override
    public boolean removeFromWords(String wordsetName, String word) {
        return WordsetsDatabase.INSTANCE.removeFromWords(wordsetName, word);
    }

    @Override
    public boolean renameWord(String wordsetName, String oldWord, String newWord) {
        return WordsetsDatabase.INSTANCE.renameWord(wordsetName, oldWord, newWord);
    }

    @Override
    public boolean renameWordset(String oldWordsetName, String newWordsetName) {
        return WordsetsDatabase.INSTANCE.renameWordset(oldWordsetName, newWordsetName);
    }
}
