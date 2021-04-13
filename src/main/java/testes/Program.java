package testes;

import mypackage.xsd.ObjectFactory;
import mypackage.xsd.TNFe;
import mypackage.xsd.TUf;
import mypackage.xsd.TVeiculo;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class Program {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Leitor NF-e");
        EntityManager em = emf.createEntityManager();



        ObjectFactory ob = new ObjectFactory();
        TVeiculo tv =  new ObjectFactory().createTVeiculo();
        tv.setUF(TUf.AC);
        tv.setPlaca("MZU-1023");
        tv.setRNTC("5981wdfsa");

        em.getTransaction().begin();

        em.persist(tv);
        em.getTransaction().commit();

        em.close();
        emf.close();
        //System.out.println(tv.toString());


    }

    public String marshal(Object object) {
        final StringWriter out = new StringWriter();
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE
            );
            marshaller.marshal(object, new StreamResult(out));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return out.toString();
    }


    /**
     * Converte o objeto em uma estrutura XML.
     * @param object objeto a ser convertido em XML.
     * @param fileName nome do arquivo XML a ser gerado.
     * @return uma string com o conteudo do XML gerado.
     */
    public String marshalToFile(Object object, String fileName) {
        final StringWriter out = new StringWriter();
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller;
            marshaller = context.createMarshaller();
            marshaller.setProperty(
                    javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE
            );
            marshaller.marshal(object, new StreamResult(out));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        Writer writer = null;
        try {
            writer = new FileWriter(fileName);
            assert context != null;
               Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(object, writer);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return out.toString();
    }



    /**
     * Realiza a conversao (unmarshal) de um arquivo XML em um objeto do seu tipo.
     * @param clazz classe referente ao objeto a ser criado a partir do XML.
     * @param fileXml nome do arquivo XML a ser convertido em objeto.
     * @return novo objeto.
     */
    public Object unmarshalFromFile(Class clazz, String fileXml) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(
                    new FileInputStream(fileXml)
            );
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converte um string com estrutura XML em um objeto.
     * @param clazz classe referente ao tipo do objeto a ser retornado.
     * @param stringXml string com o conteudo XML a ser convertido em objeto.
     * @return retorna um novo objeto de clazz.
     */
    public Object unmarshal(Class clazz, String stringXml) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(new StreamSource(new StringReader(stringXml))
            );
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
