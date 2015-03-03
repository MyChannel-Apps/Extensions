using System;
using System.Runtime.InteropServices;

namespace NppPlugin.DllExport {

	[AttributeUsage(AttributeTargets.Method, AllowMultiple = false)]
	partial class DllExportAttribute : Attribute {
		CallingConvention _callingConvention;
		string _exportName;

		public DllExportAttribute() {
			/* Do Nothing */
		}

		public DllExportAttribute(string exportName) : this(exportName, CallingConvention.StdCall) {
			/* Do Nothing */
		}

		public DllExportAttribute(string exportName, CallingConvention callingConvention) {
			ExportName			= exportName;
			CallingConvention	= callingConvention;
		}

		public CallingConvention CallingConvention {
			get {
				return _callingConvention;
			}

			set {
				_callingConvention = value;
			}
		}

		public string ExportName {
			get {
				return _exportName;
			}

			set {
				_exportName = value;
			}
		}
	}
}