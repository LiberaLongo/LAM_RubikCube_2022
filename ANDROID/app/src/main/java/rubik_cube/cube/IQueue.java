package rubik_cube.cube;

import java.util.NoSuchElementException;

public interface IQueue<E> {
	/**
	 * Inserisce un oggetto in fondo alla coda.
	 * @param x  l'oggetto da inserire.
	 * @return   l'oggetto inserito.
	 * @exception IllegalArgumentException se l'argomento passato è null
	 */
	E enqueue( E x );

	/**
	 * Rimuove e restituisce l'oggetto in testa alla coda.
	 * @return   l'oggetto in testa.
	 * @exception NoSuchElementException con coda vuota.
	 */
	E dequeue( );

	/**
	 * Restituisce l'oggetto in testa alla coda senza estrarlo.
	 * @return   l'oggetto in testa.
	 * @exception NoSuchElementException con coda vuota.
	 */
	E peek( );

	/**
	 * Verifica che la coda sia logicamente vuota.
	 * @return  true se la coda è vuota;
	 *          false altrimenti.
	 */
	boolean isEmpty( );

	/**
	 *  Svuota la coda.
	 */
	void clear( );
}
