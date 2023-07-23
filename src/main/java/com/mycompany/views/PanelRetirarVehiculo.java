/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.views;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class PanelRetirarVehiculo extends javax.swing.JPanel {

    
    public PanelRetirarVehiculo() {
        initComponents();
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        button1 = new java.awt.Button();
        tfPlacaRetiro = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(453, 400));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(777, 417));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Placa patente");

        button1.setBackground(new java.awt.Color(255, 51, 0));
        button1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setLabel("Retirar");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        tfPlacaRetiro.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 24)); // NOI18N
        jLabel1.setText("Salida de vehiculos del estacionamiento");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(34, 34, 34)
                            .addComponent(tfPlacaRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(173, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(114, 114, 114)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfPlacaRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
         Double valorAPagar = 0.0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String fechaHora = dateFormat.format(date);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/estacionamiento", "root", "");
            PreparedStatement stat = con.prepareStatement("SELECT horaentrada, tipovehiculo FROM vehiculos WHERE placa=? AND estado='Disponible'");
            stat.setString(1, tfPlacaRetiro.getText());
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                String horaSalida = rs.getString(1);
                Date horaSalidaDate = dateFormat.parse(horaSalida);
                int minutosACobrar = (int) (date.getTime() - horaSalidaDate.getTime()) / 60000;

                System.out.println(minutosACobrar);

                if (rs.getString(2).equals("Automovil")) {
                    valorAPagar = minutosACobrar * 50.0;
                } else if (rs.getString(2).equals("Motocicleta")) {
                    valorAPagar = minutosACobrar * 20.0;
                }

                System.out.println("Valor a pagar por " + rs.getString(2) + ": " + valorAPagar);

                PreparedStatement updateStat = con.prepareStatement("UPDATE vehiculos SET horasalida=?, estado='No Disponible', valorpagado=? WHERE placa=? AND estado='Disponible'");
                updateStat.setString(1, fechaHora);
                updateStat.setDouble(2, valorAPagar);
                updateStat.setString(3, tfPlacaRetiro.getText());
                updateStat.executeUpdate();

                int respuesta = JOptionPane.showConfirmDialog(null, "Valor a pagar del vehiculo: $" + valorAPagar + "\n¿Desea Imprimir Recibo?", "Salida de vehiculo", JOptionPane.YES_NO_OPTION);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el vehículo en el parqueadero. Por favor, verifique la placa ingresada.");
            }

        } catch (ClassNotFoundException | ParseException ex) {
            Logger.getLogger(PanelRetirarVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar con la base de datos. Verifica la configuración de conexión.");

            Logger.getLogger(PanelRetirarVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_button1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField tfPlacaRetiro;
    // End of variables declaration//GEN-END:variables
}
