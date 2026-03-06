package com.velocityPort;

import java.util.List;

/**
 * ListPaginator
 * Classe di utilità generica per la gestione della paginazione di una lista di elementi.
 * Permette di dividere una lista grande in sotto-liste più piccole (pagine) visualizzabili in chat o GUI.
 * 
 * @param <T> Il tipo di elementi contenuti nella lista.
 */
public class ListPaginator<T> {
   private List<T> list;
   private int perPage = 20;
   private int page = 0;

   /**
    * ListPaginator (Costruttore)
    * Crea un nuovo paginatore basato sulla lista fornita.
    * 
    * @param var1 La lista originale completa degli elementi da impaginare.
    */
   public ListPaginator(List<T> var1) {
      this.list = var1;
   }

   /**
    * perPage
    * Configura quanti elementi devono essere visualizzati per singola pagina.
    * 
    * @param var1 Numero di elementi per pagina. Se <= 0 viene impostato a 1.
    * @return L'istanza corrente del paginatore (Fluent API).
    */
   public ListPaginator perPage(int var1) {
      if (var1 <= 0) {
         var1 = 1;
      }

      this.perPage = var1;
      return this;
   }

   /**
    * page
    * Imposta l'indice della pagina di destinazione che si desidera visualizzare.
    * 
    * @param var1 L'indice della pagina (partendo da 0). Se < 0 viene impostato a 0.
    * @return L'istanza corrente del paginatore (Fluent API).
    */
   public ListPaginator page(int var1) {
      if (var1 < 0) {
         var1 = 0;
      }

      this.page = var1;
      return this;
   }

   /**
    * size
    * Restituisce il numero totale di elementi presenti nella lista originale.
    * 
    * @return Dimensione totale della lista.
    */
   public int size() {
      return this.list.size();
   }

   /**
    * pages
    * Calcola il numero totale di pagine disponibili in base alla dimensione della lista e agli elementi per pagina.
    * 
    * @return Numero totale di pagine.
    */
   public int pages() {
      return (int)Math.ceil((double)this.size() / (double)this.perPage);
   }

   /**
    * page
    * Ottiene l'indice della pagina corrente, assicurandosi che non superi i limiti della lista.
    * 
    * @return L'indice della pagina attuale o l'ultima disponibile.
    */
   public int page() {
      int var1 = this.pages();
      if (var1 == 0) {
         return 0;
      } else {
         return this.page >= var1 ? var1 - 1 : this.page;
      }
   }

   /**
    * fromIndex
    * Calcola l'indice di partenza (inclusivo) della sotto-lista per la pagina corrente.
    * 
    * @return L'indice iniziale per la sublist.
    */
   public int fromIndex() {
      return this.page() * this.perPage;
   }

   /**
    * toIndex
    * Calcola l'indice di fine (esclusivo) della sotto-lista per la pagina corrente.
    * 
    * @return L'indice finale (massimo la dimensione della lista).
    */
   public int toIndex() {
      return Math.min(this.fromIndex() + this.perPage, this.size());
   }

   /**
    * sublist
    * Estrae la porzione di lista corrispondente alla pagina attualmente selezionata.
    * 
    * @return Una List contenente gli elementi della pagina specifica.
    */
   public List<T> sublist() {
      return this.list.subList(this.fromIndex(), this.toIndex());
   }

   /**
    * list
    * Restituisce il riferimento alla lista originale completa.
    * 
    * @return La lista completa di tutti gli elementi.
    */
   public List<T> list() {
      return this.list;
   }
}
