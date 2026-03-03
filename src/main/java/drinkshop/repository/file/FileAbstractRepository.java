package drinkshop.repository.file;

import drinkshop.repository.AbstractRepository;

import java.io.*;

public abstract class FileAbstractRepository<ID, E>
        extends AbstractRepository<ID, E> {

    protected final String fileName;

    public FileAbstractRepository(String fileName) {
        this.fileName = fileName;
        // Important: keep constructor light; call loadFromFile() from subclasses/services/bootstrap
        // loadFromFile();
    }

    protected void loadFromFile() {
        File f = new File(fileName);

        try {
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!f.exists()) {
                // First run: create empty file; keep repo empty
                f.createNewFile();
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    E entity = extractEntity(line);
                    super.save(entity);
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException("Eroare la citirea fișierului: " + fileName, e);
        }
    }

    private void writeToFile() {
        File f = new File(fileName);

        try {
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                for (E entity : entities.values()) {
                    bw.write(createEntityAsString(entity));
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException("Eroare la scrierea fișierului: " + fileName, e);
        }
    }

    @Override
    public E save(E entity) {
        E e = super.save(entity);
        writeToFile();
        return e;
    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        writeToFile();
        return e;
    }

    @Override
    public E update(E entity) {
        E e = super.update(entity);
        writeToFile();
        return e;
    }

    protected abstract E extractEntity(String line);

    protected abstract String createEntityAsString(E entity);
}