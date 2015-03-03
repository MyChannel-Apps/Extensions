namespace Knuddels {
	partial class frmMyDlg {
		private System.ComponentModel.IContainer components = null;

		protected override void Dispose(bool disposing) {
			if(disposing && (components != null)) {
				components.Dispose();
			}

			base.Dispose(disposing);
		}

		#region Windows Form Designer generated code
			private void InitializeComponent() {
			this.treeView1 = new System.Windows.Forms.TreeView();
			this.SuspendLayout();
			// 
			// treeView1
			// 
			this.treeView1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
			this.treeView1.Location = new System.Drawing.Point(0, -1);
			this.treeView1.Name = "treeView1";
			this.treeView1.PathSeparator = "/";
			this.treeView1.Size = new System.Drawing.Size(283, 264);
			this.treeView1.TabIndex = 0;
			// 
			// frmMyDlg
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(284, 262);
			this.Controls.Add(this.treeView1);
			this.Name = "frmMyDlg";
			this.Text = "frmMyDlg";
			this.ResumeLayout(false);

			}
		#endregion

			private System.Windows.Forms.TreeView treeView1;

	}
}