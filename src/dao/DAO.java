/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author aluno
 */
public class DAO {
    private Connection conn =null;
    public DAO() throws Exception{
         Connection conn = Conexao.abrir();
    }
    public boolean insereCliente(Cliente cliente) throws Exception{
        try {
            String comando = "INSERT INTO pessoa (nome, cpf) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(comando);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCPF());
            cliente.setId(retornaUmCliente(cliente).getId());
            insereTelefones(cliente);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Erro ao inserir "+e);
        }
    }


    public Cliente retornaUmCliente(Cliente cliente) throws Exception{
        try {
            String comando = "SELECT * FROM pessoa  where CPF = ? ";
            PreparedStatement ps = conn.prepareStatement(comando);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCPF());
            ResultSet rs = ps.executeQuery();
            Cliente cli = new Cliente();
            rs.next();
            cli.setId(rs.getInt("id"));
            //Retornar telefones do cliente
            return cli;
        } catch (Exception e) {
            throw new Exception("Erro ao retornar um cliente"+e);
        }
    }
    
    public void insereTelefones(Cliente cliente) throws Exception{
        try {
            conn.setAutoCommit(false);
            String comando = "INSERT INTO telefones (fk_pessoa, tipo, numero) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(comando);
            for (Telefone tele: cliente.getTelefones()) {                
                ps.setInt(1, cliente.getId());
                ps.setString(2, tele.getTipoTelefone());
                ps.setString(3, tele.getTelefone());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            throw new Exception("Erro ao Inserir telefone(s)"+e);
        }
    }
   
}
