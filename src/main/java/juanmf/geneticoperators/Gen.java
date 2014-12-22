/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.geneticoperators;

import java.io.Serializable;

/**
 *
 * @author juan.fernandez
 */
public interface Gen<E> extends Serializable {
    public E getValue();
}
